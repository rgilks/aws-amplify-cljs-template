# AWS Amplify ClojureScript Template

A ClojureScript SPA template for building real-time multi-user apps on AWS Amplify.

## Current Status

This template targets the classic Amplify CLI / DataStore stack with Amplify JS v6, Amplify UI React v6, React 18, MUI 5, Shadow CLJS, Karma, and Cypress.

It has been refreshed to current patch/minor versions within that architecture. Larger migrations such as Amplify Gen 2, React 19, MUI 6+, Shadow CLJS 3, and Cypress 15 should be handled as explicit project migrations rather than hidden template updates.

## Architecture

This project uses a dual-compilation approach:

1. Shadow CLJS compiles ClojureScript to JavaScript using `:js-provider :external`, which leaves JavaScript `require()` calls unresolved in the output.
2. Webpack bundles npm dependencies such as React, MUI, and Amplify into `dist/js/libs/bundle.js`.
3. The browser loads both outputs, and the Shadow CLJS code calls into the Webpack bundle for npm dependencies.

This keeps ClojureScript hot reload fast while preserving access to the npm ecosystem.

## Features

- AWS Amplify Auth, AppSync, and DataStore integration
- Amplify Studio generated model and form files
- MUI 5 for styling
- Shadow CLJS development and release builds
- Karma unit tests and Cypress end-to-end tests
- Amplify Console build image support

## Requirements

- Node.js >= 20.0.0 (use `nvm use` with the included `.nvmrc`)
- Yarn 1.22.x
- Java 17+ for Shadow CLJS and the ClojureScript compiler
- Clojure CLI
- Amplify CLI installed separately with the official AWS instructions

## Template Customization Checklist

After forking this template, replace these placeholders:

| Placeholder | Location | Replace with |
|---|---|---|
| `{{APP_NAME}}` | `public/index.html.tmpl`, `public/manifest.json` | Your app name |
| `{{APP_SHORT_NAME}}` | `public/manifest.json` | Short app name for PWA installs |
| `{{APP_DESCRIPTION}}` | `public/index.html.tmpl` | App description |
| `APP TITLE` | `src/cljs/app/games.cljs` | Your app title |
| `APP NAME` | Cypress tests in `cypress/e2e/` | Match your `<title>` tag |
| `APP INFO` | `public/info.html`, Cypress tests | Your info page title |
| `aws-amplify-cljs-template` | `package.json` | Your project name |
| `cljstemplate` | Amplify config files | Your Amplify project name |

Also:

- Copy `src/amplify/aws-exports.js.example` to `src/amplify/aws-exports.js` and fill in your Amplify project values, or run `amplify push` to generate it.
- Update logos in `public/`, including `favicon.ico`, `logo*.png`, and `login-title.png`.

## Quick Start

```bash
yarn install
yarn dev
```

Open `http://localhost:3000` in your browser.

For a single release-and-test pass:

```bash
yarn ci
```

## Available Scripts

| Script | Description |
|---|---|
| `yarn dev` | Start Shadow CLJS and Webpack concurrently |
| `yarn watch` | Start Shadow CLJS dev server on port 3000 |
| `yarn webpack` | Start Webpack in watch mode |
| `yarn karma` | Start Karma test runner |
| `yarn build` | Production Shadow CLJS release build |
| `yarn ci` | Full CI pipeline: build, bundle, and tests |
| `yarn lint` | Run clj-kondo |
| `yarn format` | Format code with cljfmt and Prettier, then lint |
| `yarn cypress` | Open Cypress test runner |
| `yarn cypress:run` | Run Cypress tests headless |

## Setting Up a New Project with AWS Amplify

### 1. Create New Repository from Template

1. Visit the [AWS Amplify CLJS Template](https://github.com/rgilks/aws-amplify-cljs-template) on GitHub.
2. Click "Use this template".
3. Fill in `myproject` as the name for your new repository.
4. Click "Create repository".

### 2. Connect Repository to AWS Amplify

1. Navigate to your GitHub account settings.
2. Go to Integrations > Applications > AWS Amplify > Configure.
3. Add your `myproject` repository to the AWS Amplify application and save your changes.

### 3. Rename and Set Up Project Files

1. Navigate to your `myproject` directory and run `grep -r cljstemplate .`.
2. Rename the `amplify/backend/api/cljstemplate` folder to `amplify/backend/api/myproject`.
3. Rename the `amplify/backend/auth/cljstemplatecc274de4` folder to `amplify/backend/auth/myprojectcc274de4`.
4. Keep `amplify/team-provider-info.json` empty until `amplify init` writes your own environment details.
5. Find and replace `cljstemplate` with `myproject` across the project. In particular, check Amplify config files such as `project-config.json`.
6. Run `amplify init`.
7. When asked for the environment name, use a name like `devmyprojecta`.
8. Run `amplify push` and accept the generated backend changes.
9. Run `yarn install`.
10. Run `yarn dev` and ensure the app loads at `http://localhost:3000`.
11. In a separate terminal, run `yarn karma` and ensure all tests pass.
12. Connect your main branch in the Amplify Console hosting environments.

### 4. Update Package Metadata

1. Change the package name in `package.json` from `aws-amplify-cljs-template` to your project name.
2. Run `yarn install` to refresh dependency metadata.

### 5. Create Test Users and Run Cypress

1. Create test user accounts `testUser1` and `testUser2` with passwords from your Cypress config.
2. Store the Cypress config in AWS Secrets Manager as `cypress/config`.
3. Run `yarn cypress:run`.

## Cypress Tests

Some Cypress tests check that an email is received. To set this up:

1. Copy `cypress-config-example.json` to `cypress-config.json` and update the details.
2. Create a new Gmail account and set up OAuth credentials following the [gmail-tester](https://github.com/levz0r/gmail-tester) instructions.
3. Store the config in AWS Secrets Manager as `cypress/config`.
4. Create test user accounts `testUser1` and `testUser2` in your app.
5. Set the `CYPRESS_CONFIG` environment variable in Amplify Console if your build needs it.

## Key Technologies

### JavaScript

- [AWS Amplify v6](https://docs.amplify.aws/gen1/) for Auth, DataStore, and UI integration
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

## DataStore

The project uses AWS Amplify DataStore for managing application data.

![DataStore](docs/datastore.webp?raw=true)

## Amplify Build Image

The `Dockerfile` constructs the build image for the Amplify Console with Amazon Linux 2023, Node 20, and Java 17.

`public.ecr.aws/n1r2w5d4/tre-amplify-custom-image` is available publicly.

To build and deploy your own:

```bash
aws ecr-public get-login-password --region us-east-1 | docker login --username AWS --password-stdin public.ecr.aws/YOUR_REGISTRY
docker buildx build --platform linux/amd64 --push -t public.ecr.aws/YOUR_REGISTRY/YOUR_IMAGE .
```

## Amplify Console Configuration

Set a `BASE_PATH` environment variable for each subdomain.

Set up redirects for SPA routing:

```text
</^(/game)[^.]+$|\.(?!(html|htm|mp3|bin|gltf|css|gif|ico|jpg|js|png|txt|svg|woff|ttf|map|json)$)([^.]+$)/>
</^(/email-settings)[^.]+$|\.(?!(html|htm|mp3|bin|gltf|css|gif|ico|jpg|js|png|txt|svg|woff|ttf|map|json)$)([^.]+$)/>
```

## Troubleshooting

**Shadow CLJS will not start**: Ensure Java 17+ is installed with `java -version`.

**Webpack errors about missing modules**: Run `yarn install`. If you see errors about `aws-exports`, copy the example file with `cp src/amplify/aws-exports.js.example src/amplify/aws-exports.js`.

**Hot reload not working**: The `^:dev/after-load` hook in `core.cljs` clears the refx subscription cache and re-renders. If state seems stale, try a full page refresh.

**DataStore sync issues**: Check the browser console for DataStore Hub events. The app listens for the `ready` event before querying data.

## License

This project is licensed under the [MIT License](LICENSE).
