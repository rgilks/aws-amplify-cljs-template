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
