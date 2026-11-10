const {defineConfig} = require('cypress')
const {createServer} = require('node:http')
const {readFile, realpath} = require('node:fs/promises')
const path = require('node:path')

if (process.env.TEMPLATE_OFFLINE_TEST !== '1') {
  throw new Error(
    'Offline Cypress requires TEMPLATE_OFFLINE_TEST=1 and an offline build'
  )
}

module.exports = defineConfig({
  video: false,
  screenshotOnRunFailure: false,
  e2e: {
    specPattern: 'test/cypress/offline.cy.cjs',
    supportFile: false,
    async setupNodeEvents(on, config) {
      const root = await realpath(path.join(__dirname, 'dist'))
      const html = await readFile(path.join(root, 'index.html'), 'utf8')
      if (!/<base href="\/"\s*\/>/.test(html)) {
        throw new Error(
          'Build with TEMPLATE_OFFLINE_TEST=1 BASE_PATH=/ before offline Cypress'
        )
      }
      const types = {
        '.html': 'text/html',
        '.js': 'text/javascript',
        '.css': 'text/css',
        '.svg': 'image/svg+xml',
        '.ico': 'image/x-icon',
        '.json': 'application/json'
      }
      const server = createServer(async (request, response) => {
        if (!['GET', 'HEAD'].includes(request.method)) {
          response.writeHead(405).end()
          return
        }
        try {
          const pathname = decodeURIComponent(
            new URL(request.url, 'http://localhost').pathname
          )
          const file = await realpath(
            path.resolve(
              root,
              '.' + (pathname === '/' ? '/index.html' : pathname)
            )
          )
          if (!file.startsWith(root + path.sep)) {
            response.writeHead(403).end()
            return
          }
          const body = await readFile(file)
          response.writeHead(200, {
            'content-type':
              types[path.extname(file)] || 'application/octet-stream'
          })
          response.end(request.method === 'HEAD' ? undefined : body)
        } catch {
          response.writeHead(404).end()
        }
      })
      await new Promise((resolve, reject) => {
        server.once('error', reject)
        server.listen(0, '127.0.0.1', resolve)
      })
      on(
        'after:run',
        () =>
          new Promise(resolve => {
            server.closeAllConnections()
            server.close(resolve)
          })
      )
      config.baseUrl = `http://127.0.0.1:${server.address().port}`
      config.env = {offlineFixture: 'local-only'}
      return config
    }
  }
})
