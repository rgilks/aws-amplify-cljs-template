# AWS Amplify ClojureScript Template

A ClojureScript SPA template for building real-time multi-user apps on AWS Amplify.

## Architecture

This project uses a dual-compilation approach:

1. **Shadow-CLJS** compiles ClojureScript to JavaScript using `:js-provider :external`, which means JS `require()` calls are left unresolved in the output
2. **Webpack** bundles all npm dependencies (React, MUI, Amplify, etc.) into a separate `bundle.js`
3. Both outputs are loaded by the browser - Shadow-CLJS output calls into the Webpack bundle for npm deps

This architecture gives you fast ClojureScript hot-reload (Shadow-CLJS only recompiles CLJS) while still having full access to the npm ecosystem.

## Requirements

- Node.js >= 20.0.0 (use `nvm use` with the included `.nvmrc`)
- Yarn 1.x
- Java 17+ (for Shadow-CLJS / ClojureScript compiler)

## Template Customization Checklist

After forking this template, replace these placeholders:

| Placeholder | Location | Replace with |
|---|---|---|
| `{{APP_NAME}}` | `public/index.html.tmpl`, `public/manifest.json` | Your app name |
| `{{APP_SHORT_NAME}}` | `public/manifest.json` | Short app name (for PWA) |
| `{{APP_DESCRIPTION}}` | `public/index.html.tmpl` | App description |
| `APP TITLE` | `src/cljs/app/games.cljs` | Your app title |
| `APP NAME` | Cypress tests (`cypress/e2e/`) | Match your `<title>` tag |
| `APP INFO` | `public/info.html`, Cypress tests | Your info page title |
| `aws-amplify-cljs-template` | `package.json` | Your project name |
| `cljstemplate` | Amplify config files | Your Amplify project name |

Also:
- Copy `src/amplify/aws-exports.js.example` to `src/amplify/aws-exports.js` and fill in your Amplify project values (or run `amplify push` to generate it)
- Update logos in `public/` (`favicon.ico`, `logo*.png`, `login-title.png`)

## Quick Start

```bash
# Install dependencies
yarn install

# Start development (Shadow-CLJS + Webpack in parallel)
yarn dev

# Or start each in separate terminals:
yarn watch    # Shadow-CLJS dev server on port 3000
yarn webpack  # Webpack bundler in watch mode

# Run unit tests
yarn karma
```

## Running the Project

1. **Start dev servers:** `yarn dev` (runs `yarn watch` + `yarn webpack` concurrently)
2. Open `http://localhost:3000` in your browser
3. **Run tests:** `yarn karma` in another terminal

## Available Scripts

| Script | Description |
|---|---|
| `yarn dev` | Start Shadow-CLJS + Webpack concurrently |
| `yarn watch` | Start Shadow-CLJS dev server (port 3000) |
| `yarn webpack` | Start Webpack in watch mode |
| `yarn karma` | Start Karma test runner |
| `yarn build` | Production build (Shadow-CLJS release) |
| `yarn ci` | Full CI pipeline (build + tests) |
| `yarn lint` | Run clj-kondo linter |
| `yarn format` | Format code (cljfmt + prettier + lint) |
| `yarn cypress` | Open Cypress test runner |
| `yarn cypress:run` | Run Cypress tests headless |

## Setting Up a New Project with AWS Amplify

### 1. Create New Repository from Template

1. Visit the [AWS Amplify CLJS Template](https://github.com/rgilks/aws-amplify-cljs-template) on GitHub.
2. Click on "Use this template".
3. Fill in 'myproject' as the name for your new repository.
4. Click on "Create repository".

### 2. Connect Repository to AWS Amplify

1. Navigate to your GitHub account settings.
2. Go to Integrations > Applications > AWS Amplify > configure.
3. Add your 'myproject' repository to the AWS Amplify application and save your changes.

### 3. Rename and Setup Project Files

1. Navigate to your 'myproject' directory and execute `grep -r cljstemplate .`
2. Rename the `api/cljstemplate` folder to `api/myproject`.
3. Rename the `auth/cljstemplatecc274de4` folder to `api/myprojectc274de4`.
4. Empty the content of the `team-provider-info.json` file.
5. Find and replace `cljstemplate` with `myproject` across your project.
6. Run `amplify init`.
7. When asked for the environment name, use something like `devmyprojecta`.
8. Run `amplify push` and agree to the questions asked.
9. Run `yarn install` to install project dependencies.
10. Run `yarn dev` and ensure the app loads at localhost:3000.
11. In a separate terminal, run `yarn karma` and ensure all tests pass.
12. Connect your main branch in the Amplify Console hosting environments.

### 4. Update Package.json

1. Change the package name in `package.json` from `aws-amplify-cljs-template` to `myproject`.
2. Run `yarn install`.

## Cypress Tests

Some Cypress tests check that an email is received. To set this up:

1. Copy `cypress-config-example.json` to `cypress-config.json` and update the details
2. Create a new Gmail account and set up OAuth credentials following [gmail-tester](https://github.com/levz0r/gmail-tester) instructions
3. Store the config in AWS Secrets Manager as `cypress/config`
4. Create test user accounts `testUser1` and `testUser2` in your app
5. Set `CYPRESS_CONFIG` environment variable in Amplify Console

See the [gmail-tester docs](https://github.com/levz0r/gmail-tester) for detailed OAuth setup instructions.

## Key Technologies

### JavaScript
- [AWS Amplify v6](https://docs.amplify.aws/gen1/) - Auth, DataStore, UI components
- [React 18](https://18.react.dev/) - UI rendering
- [MUI 5](https://v5.mui.com/) - Material Design components
- [Webpack 5](https://webpack.js.org/) - Module bundling

### ClojureScript
- [Shadow-CLJS](https://shadow-cljs.github.io/docs/UsersGuide.html) - ClojureScript compiler with npm integration
- [UIx](https://github.com/pitch-io/uix) - Idiomatic ClojureScript interface to React
- [refx](https://github.com/ferdinand-beyer/refx) - State management (re-frame compatible)
- [Reitit](https://metosin.github.io/reitit/) - Data-driven routing
- [Promesa](https://funcool.github.io/promesa/latest/) - Promise library
- [Malli](https://github.com/metosin/malli) - Data-driven schemas

## Amplify Build Image

The `Dockerfile` constructs the build image for the Amplify Console (Amazon Linux 2023, Node 20, Java 17).

`public.ecr.aws/n1r2w5d4/tre-amplify-custom-image` is available publicly.

To build and deploy your own:

```bash
# Authenticate with ECR
aws ecr-public get-login-password --region us-east-1 | docker login --username AWS --password-stdin public.ecr.aws/YOUR_REGISTRY

# Build and push
docker buildx build --platform linux/amd64 --push -t public.ecr.aws/YOUR_REGISTRY/YOUR_IMAGE .
```

## Amplify Console Configuration

Set a `BASE_PATH` environment variable for each subdomain.

Set up redirects for SPA routing:

```
</^(/game)[^.]+$|\.(?!(html|htm|mp3|bin|gltf|css|gif|ico|jpg|js|png|txt|svg|woff|ttf|map|json)$)([^.]+$)/>
</^(/email-settings)[^.]+$|\.(?!(html|htm|mp3|bin|gltf|css|gif|ico|jpg|js|png|txt|svg|woff|ttf|map|json)$)([^.]+$)/>
```

## Troubleshooting

**Shadow-CLJS won't start**: Ensure Java 17+ is installed (`java -version`). Shadow-CLJS requires a JVM.

**Webpack errors about missing modules**: Run `yarn install` to ensure all npm deps are installed. If you see errors about `aws-exports`, copy the example file: `cp src/amplify/aws-exports.js.example src/amplify/aws-exports.js`

**Hot reload not working**: The `^:dev/after-load` hook in `core.cljs` clears the refx subscription cache and re-renders. If state seems stale, try a full page refresh.

**DataStore sync issues**: Check the browser console for DataStore hub events. The app listens for the `"ready"` event before querying data.

## License

This project is licensed under the [GNU General Public License v3.0](LICENSE).
