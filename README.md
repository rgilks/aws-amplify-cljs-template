# AWS Amplify ClojureScript Template

A ClojureScript single-page app template for real-time, multi-user applications on AWS Amplify.

## Current Status

This template uses the Amplify Gen 1 / classic Amplify CLI stack with Amplify JS v6, Amplify UI React v6, React 18, MUI 5, Shadow CLJS, Karma, and Cypress.

The dependencies have been refreshed within that architecture. Bigger moves, such as Amplify Gen 2, React 19, MUI 6+, Shadow CLJS 3, or Cypress 15, should be treated as deliberate project migrations.

## Architecture

The app uses a dual-compilation setup:

1. Shadow CLJS compiles the ClojureScript app to `dist/js/main.js` using `:js-provider :external`.
2. Webpack bundles npm dependencies such as React, MUI, and Amplify into `dist/js/libs/bundle.js`.
3. The browser loads both outputs, and Shadow CLJS calls into the Webpack bundle for npm modules.

This keeps ClojureScript hot reload fast while still allowing normal npm package use.

## Requirements

- Node.js >= 20.0.0. Use `nvm use` with the included `.nvmrc`.
- Yarn 1.22.x.
- Java 17+ for Shadow CLJS and the ClojureScript compiler.
- Clojure CLI.
- Amplify CLI, installed and configured with AWS credentials. See the official [Amplify CLI setup guide](https://docs.amplify.aws/gen1/javascript/tools/cli/start/set-up-cli/).

## Project Layout

| Path               | Purpose                                                           |
| ------------------ | ----------------------------------------------------------------- |
| `src/cljs/app/`    | ClojureScript application code                                    |
| `src/amplify/`     | Amplify-generated frontend integration files                      |
| `amplify/backend/` | Amplify Gen 1 backend resources                                   |
| `public/`          | Static files copied into `dist/` by Webpack                       |
| `test/`            | Karma unit tests                                                  |
| `cypress/`         | Cypress end-to-end tests                                          |
| `emails/`          | SES email templates used by `scripts/update_email_templates.cljs` |
| `docs/`            | Documentation images used by this README                          |

## Quick Start

For a fresh clone that only needs to compile and run locally, create a placeholder Amplify config before starting the dev servers:

```bash
yarn install
cp src/amplify/aws-exports.js.example src/amplify/aws-exports.js
yarn dev
```

Open `http://localhost:3000`.

For a real project backend, complete the Amplify setup steps below before relying on Auth or DataStore behavior.

For a full release-and-test pass:

```bash
yarn ci
```

## Available Scripts

| Script             | Description                                              |
| ------------------ | -------------------------------------------------------- |
| `yarn dev`         | Start Shadow CLJS and Webpack concurrently               |
| `yarn watch`       | Start Shadow CLJS dev server on port 3000                |
| `yarn webpack`     | Start Webpack in watch mode                              |
| `yarn karma`       | Start Karma test runner                                  |
| `yarn build`       | Production Shadow CLJS release build                     |
| `yarn ci`          | Full CI pipeline: release build, bundle, and Karma tests |
| `yarn lint`        | Run clj-kondo                                            |
| `yarn format`      | Format code with cljfmt and Prettier, then lint          |
| `yarn cypress`     | Open Cypress test runner                                 |
| `yarn cypress:run` | Run Cypress tests headless                               |

## Creating a Project from This Template

### 1. Create the Repository

1. Open the [AWS Amplify ClojureScript Template](https://github.com/rgilks/aws-amplify-cljs-template) on GitHub.
2. Click "Use this template".
3. Create your new repository, for example `myproject`.
4. Clone the new repository locally.

### 2. Rename Template Placeholders

Replace the template placeholders before creating your Amplify environment.

| Placeholder                 | Location                                                   | Replace with               |
| --------------------------- | ---------------------------------------------------------- | -------------------------- |
| `aws-amplify-cljs-template` | `package.json`                                             | Your package name          |
| `cljstemplate`              | Amplify config, `.graphqlconfig.yml`, backend folder names | Your Amplify project name  |
| `cljstemplatecc274de4`      | Auth resource folder and backend references                | Your renamed Auth resource |
| `{{APP_NAME}}`              | `public/index.html.tmpl`, `public/manifest.json`           | Your app name              |
| `{{APP_SHORT_NAME}}`        | `public/manifest.json`                                     | Short PWA name             |
| `{{APP_DESCRIPTION}}`       | `public/index.html.tmpl`                                   | App description            |
| `APP TITLE`                 | `src/cljs/app/games.cljs`                                  | Main app title             |
| `APP NAME`                  | Cypress tests in `cypress/e2e/`                            | Browser page title         |
| `APP INFO`                  | `public/info.html`, Cypress tests                          | Info page title            |

Recommended folder renames:

```bash
mv amplify/backend/api/cljstemplate amplify/backend/api/myproject
mv amplify/backend/auth/cljstemplatecc274de4 amplify/backend/auth/myprojectcc274de4
```

After renaming folders, search for remaining placeholders:

```bash
rg 'cljstemplate|APP NAME|APP TITLE|APP INFO|{{APP_'
```

Keep `amplify/team-provider-info.json` as `{}` in the template. `amplify init` will write your local environment details.

### 3. Initialize Amplify

1. Run `amplify init`.
2. Use an environment name such as `devmyprojecta`.
3. Select your editor and AWS profile when prompted.
4. Run `amplify push` and accept the generated backend changes.
5. Confirm `src/amplify/aws-exports.js` was generated.

Do not commit `src/amplify/aws-exports.js`; it is environment-specific and ignored by Git.

### 4. Install and Run

```bash
yarn install
yarn dev
```

Open `http://localhost:3000`, then run `yarn karma` in another terminal.

### 5. Configure Amplify Hosting

1. Open the Amplify Console for your AWS account.
2. Connect your GitHub repository and main branch.
3. Use the included `amplify.yml`.
4. Set `BASE_PATH` for the deployed subdomain if your app is not served from `/`.
5. Set `CYPRESS_CONFIG` as an Amplify Console environment variable if you run Cypress in the hosted test phase.

## Cypress Configuration

Cypress needs a JSON config with Cognito and Gmail test-account details. Use `cypress-config-example.json` as the shape.

For local runs, store the JSON in AWS Secrets Manager as `cypress/config`. The `yarn cypress` and `yarn cypress:run` scripts read that secret into `CYPRESS_CONFIG`.

For Amplify Console test runs, add the same JSON value as the `CYPRESS_CONFIG` environment variable.

The test users expected by the current specs are:

- `testUser1`
- `testUser2`

Some tests poll Gmail for verification emails through [gmail-tester](https://github.com/levz0r/gmail-tester), so those users need valid Gmail OAuth credentials in the config.

## DataStore

The project uses AWS Amplify DataStore for application data.

![DataStore](docs/datastore.webp?raw=true)

## Amplify Build Image

The `Dockerfile` builds an Amplify Console image with Amazon Linux 2023, Node 20, Java 17, Yarn, Chrome, Cypress, Clojure CLI, and the Amplify CLI.

The public image is:

```text
public.ecr.aws/n1r2w5d4/tre-amplify-custom-image
```

To build and push your own image:

```bash
aws ecr-public get-login-password --region us-east-1 | docker login --username AWS --password-stdin public.ecr.aws/YOUR_REGISTRY
docker buildx build --platform linux/amd64 --push -t public.ecr.aws/YOUR_REGISTRY/YOUR_IMAGE .
```

## Amplify Console Rewrites

Set up SPA redirects for app routes such as `/game/:slug` and `/email-settings`:

```text
</^(/game)[^.]+$|\.(?!(html|htm|mp3|bin|gltf|css|gif|ico|jpg|js|png|txt|svg|woff|ttf|map|json)$)([^.]+$)/>
</^(/email-settings)[^.]+$|\.(?!(html|htm|mp3|bin|gltf|css|gif|ico|jpg|js|png|txt|svg|woff|ttf|map|json)$)([^.]+$)/>
```

## Key Technologies

### JavaScript

- [AWS Amplify v6](https://docs.amplify.aws/gen1/javascript/build-a-backend/) for Auth, DataStore, and UI integration
- [React 18](https://18.react.dev/) for UI rendering
- [MUI 5](https://v5.mui.com/) for Material Design components
- [Howler.js](https://howlerjs.com/) for audio helpers
- [React Testing Library](https://testing-library.com/docs/react-testing-library/intro/) for component tests
- [Webpack 5](https://webpack.js.org/) for module bundling

### ClojureScript

- [Shadow CLJS](https://shadow-cljs.github.io/docs/UsersGuide.html) for ClojureScript compilation
- [UIx](https://pitch-io.github.io/uix/) for React integration
- [refx](https://github.com/fbeyer/refx) for local state management
- [Reitit](https://metosin.github.io/reitit/) for routing
- [Promesa](https://funcool.github.io/promesa/latest/) for promises
- [Malli](https://github.com/metosin/malli) for schemas

## Generated and Local Files

These files and folders are generated locally and should not be committed:

- `dist/`
- `target/`
- `.shadow-cljs/`
- `karma/js/`
- `node_modules/`
- `src/amplify/aws-exports.js`
- `cypress-config.json`
- Cypress screenshots and videos

## Troubleshooting

**Shadow CLJS will not start**: Check Java with `java -version`; Java 17 or newer is required.

**Webpack cannot resolve `aws-exports`**: Create a local config with `cp src/amplify/aws-exports.js.example src/amplify/aws-exports.js`, or run `amplify push` after `amplify init`.

**Auth or DataStore calls fail locally**: Confirm `src/amplify/aws-exports.js` contains real values from your Amplify environment, not the placeholder example values.

**Hot reload looks stale**: The `^:dev/after-load` hook in `core.cljs` clears the refx subscription cache and re-renders. If state still looks stale, refresh the page.

**Cypress cannot start**: Confirm `CYPRESS_CONFIG` is valid JSON and contains `region`, `userPoolId`, and `testUsers`.

## License

This project is licensed under the [MIT License](LICENSE).
