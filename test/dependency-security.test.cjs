const assert = require('node:assert/strict')
const test = require('node:test')
const {createRequire} = require('node:module')
const {spawnSync} = require('node:child_process')
const semver = require('semver')
const fs = require('node:fs')
const os = require('node:os')
const path = require('node:path')

function from(parent, name) {
  return createRequire(require.resolve(parent))(name)
}

test('actual HTTP and build consumers resolve patched compatible dependencies', () => {
  const expectations = [
    ['wait-on', 'axios', '>=1.20.0 <2'],
    ['@cypress/request', 'form-data', '>=4.0.6 <5'],
    ['concurrently', 'shell-quote', '>=1.9.0 <2'],
    ['http-proxy', 'follow-redirects', '>=1.16.2 <2'],
    ['socket.io-adapter', 'ws', '>=8.21.0 <9'],
    ['wait-on', 'joi', '>=18.3.1 <19'],
    ['terser-webpack-plugin', 'serialize-javascript', '>=7.1.4 <8']
  ]
  for (const [parent, name, range] of expectations) {
    const version = from(parent, name + '/package.json').version
    assert.ok(semver.satisfies(version, range), `${parent}: ${name} ${version}`)
  }
})

test('patched shell quoting preserves ordinary argument data', () => {
  const quote = from('concurrently', 'shell-quote')
  const args = [
    'hello world',
    "single'quote",
    'semi;colon',
    'line\nbreak',
    '$literal'
  ]
  assert.deepEqual(quote.parse(quote.quote(args), {}), args)
})

test('Cypress uses the supported archive reader instead of extract-zip', () => {
  assert.ok(
    semver.satisfies(require('cypress/package.json').version, '>=16.2.1 <17')
  )
  assert.ok(
    semver.satisfies(
      from('cypress', 'yauzl/package.json').version,
      '>=3.3.1 <4'
    )
  )
  assert.throws(() => require.resolve('extract-zip'), {
    code: 'MODULE_NOT_FOUND'
  })
})

test('Shadow CLJS uses its supported compiler without obsolete browser crypto shims', () => {
  assert.ok(
    semver.satisfies(require('shadow-cljs/package.json').version, '>=3.5.13 <4')
  )
  for (const name of ['elliptic', 'node-libs-browser']) {
    assert.throws(() => require.resolve(name), {code: 'MODULE_NOT_FOUND'})
  }
})

test('legacy integration config loads and registers tasks without invoking cloud services', () => {
  const child = spawnSync(
    process.execPath,
    [
      '-e',
      `
    const assert = require('node:assert/strict')
    const config = require('./cypress.config')
    const tasks = {}
    const result = config.e2e.setupNodeEvents((event, values) => {
      assert.equal(event, 'task')
      Object.assign(tasks, values)
    }, {env: {CONFIG: {private: 'fixture-private-marker'}}})
    assert.equal(typeof tasks.getEmailMessages, 'function')
    assert.equal(typeof tasks.deleteTestUser, 'function')
    assert.throws(() => tasks.getEmailMessages({username: 'unknown'}), /Unknown test-account alias/)
    Promise.resolve(result).then(value => {
      assert.equal(value.env.CONFIG, undefined)
      assert.deepEqual(value.env['testUser:fixture'], {email: 'fixture-address', password: 'fixture-password'})
      assert.ok(!JSON.stringify(value.env).includes('fixture-private-marker'))
    })
  `
    ],
    {
      encoding: 'utf8',
      timeout: 10000,
      env: {
        PATH: process.env.PATH,
        CYPRESS_CONFIG: JSON.stringify({
          region: 'us-east-1',
          userPoolId: 'offline-fixture',
          testUsers: {
            fixture: {
              email: 'fixture-address',
              password: 'fixture-password',
              creds: {private: 'fixture-private-marker'},
              token: {private: 'fixture-private-marker'}
            }
          }
        })
      }
    }
  )
  assert.equal(child.status, 0, child.stderr)
  for (const file of ['auth.cy.js', 'games.cy.js']) {
    const source = fs.readFileSync(path.join('cypress/e2e', file), 'utf8')
    assert.doesNotMatch(source, /Cypress\.env\(/)
    assert.match(source, /cy\.env\(/)
  }
})

test('concurrent command runner preserves success and failure propagation', () => {
  const resolveConcurrent = createRequire(require.resolve('concurrently'))
  const cli = resolveConcurrent.resolve(
    resolveConcurrent('./package.json').bin.concurrently
  )
  for (const code of [0, 1]) {
    const child = spawnSync(
      process.execPath,
      [
        cli,
        '--success',
        'all',
        'node -e "process.exit(0)"',
        `node -e "process.exit(${code})"`
      ],
      {encoding: 'utf8', timeout: 10000}
    )
    assert.equal(child.status, code)
  }
})

test('beautifier glob and EditorConfig consumers use patched Minimatch without changing legacy consumers', () => {
  const beautifyRequire = createRequire(require.resolve('js-beautify'))
  for (const parent of ['glob', 'editorconfig']) {
    const consumer = createRequire(beautifyRequire.resolve(parent))
    assert.ok(
      semver.satisfies(
        consumer('minimatch/package.json').version,
        '>=11.0.1 <12'
      )
    )
    const {Minimatch} = consumer('minimatch')
    const pattern = new Minimatch('**/*.{html,js}')
    assert.ok(pattern.match('nested/fixture.html'))
    assert.ok(pattern.match('fixture.js'))
    assert.ok(!pattern.match('fixture.css'))
  }
  assert.ok(
    semver.satisfies(
      from('karma', 'minimatch/package.json').version,
      '>=3.1.5 <4'
    )
  )
})

test('actual beautifier CLI expands file globs and respects EditorConfig settings', () => {
  const root = fs.mkdtempSync(path.join(os.tmpdir(), 'template-beautifier-'))
  try {
    fs.writeFileSync(
      path.join(root, '.editorconfig'),
      'root = true\n\n[*]\nindent_style = space\nindent_size = 4\n'
    )
    for (const name of ['first.html', 'second.html'])
      fs.writeFileSync(
        path.join(root, name),
        '<html><body><div>fixture</div></body></html>'
      )
    const untouched = path.join(root, 'untouched.js')
    fs.writeFileSync(untouched, 'const fixture=1;')
    const packageFile = require.resolve('js-beautify/package.json')
    const cli = path.resolve(
      path.dirname(packageFile),
      require(packageFile).bin['html-beautify']
    )
    const child = spawnSync(
      process.execPath,
      [cli, '-f', '*.html', '--replace', '--editorconfig'],
      {cwd: root, encoding: 'utf8', timeout: 10000}
    )
    assert.equal(child.status, 0, child.stderr)
    for (const name of ['first.html', 'second.html'])
      assert.match(
        fs.readFileSync(path.join(root, name), 'utf8'),
        /\n {4}<div>fixture<\/div>/
      )
    assert.equal(fs.readFileSync(untouched, 'utf8'), 'const fixture=1;')
  } finally {
    fs.rmSync(root, {recursive: true, force: true})
  }
})
