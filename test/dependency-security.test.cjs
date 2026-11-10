const assert = require('node:assert/strict')
const test = require('node:test')
const {createRequire} = require('node:module')
const {spawnSync} = require('node:child_process')
const semver = require('semver')

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
