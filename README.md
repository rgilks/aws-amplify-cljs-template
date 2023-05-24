# AWS Amplify ClojureScript Template

This is a ClojureScript project template for AWS Amplify. This project is set up with AWS Amplify, Amplify UI components, React, Material UI, emotion for CSS-in-JS, and various other dependencies.

## Features

- AWS Amplify integration
- Uses Material UI and emotion for styling
- Code formatting with Prettier
- Linting with clj-kondo
- Testing with Karma
- Continuous integration set up with Amplify

## Requirements

This project requires Node.js and npm. The required versions are:

- Node.js: >=16.0.0
- npm: >=8.0.0

## Initial Setup

1. Install Amplify CLI: If you haven't used Amplify before, or don't have a profile set up on the AWS account you plan to use, follow the [Amplify CLI Installation Guide](https://docs.amplify.aws/cli/start/install/).

2. Setup Amplify: Follow the guide on [Amplify CLI Workflows](https://docs.amplify.aws/cli/start/workflows/) to set up Amplify for your project.

## Running the Project

To run the project, you need to start webpack, the development server, and the Karma test runner in separate terminals.

1. Start the development server:

   In a separate terminal, run `yarn watch`.

2. Start webpack:

   In a terminal, run `yarn webpack`. Note that you might see some errors initially until the build processes are complete.

3. Start the Karma test runner:

   In a third terminal, run `yarn karma`.

## DataStore

The project uses AWS Amplify DataStore for managing application data.

![DataStore](/docs/datastore.webp?raw=true)

# JavaScript Technologies

This project primarily relies on several JavaScript technologies and libraries. The key dependencies are:

- [AWS Amplify](https://aws.amazon.com/amplify/): A set of tools and services that enables developers to build scalable and secure cloud applications. The project uses `@aws-amplify/auth` for authentication, `@aws-amplify/datastore` for data storage and synchronization, and `@aws-amplify/ui-react` for user interface components.

- [React](https://reactjs.org/): A JavaScript library for building user interfaces. The project also uses `react-dom` for DOM-specific methods and `react-test-renderer` for testing.

- [Material-UI](https://mui.com/): A popular React UI framework. The project uses `@material-ui/core` and `@mui/material` for core components, `@material-ui/icons` and `@mui/icons-material` for icons, `@mui/lab` for advanced UI components, `@mui/styles` for styling, and `@mui/system` for utility functions.

- [React JSON Schema Form (rjsf)](https://github.com/rjsf-team/react-jsonschema-form): A React component for building Web forms from JSON Schema. The project uses `@rjsf/core`, `@rjsf/mui`, `@rjsf/utils`, and `@rjsf/validator-ajv6`.

- [Howler.js](https://howlerjs.com/): An audio library for the modern web.

- [React Testing Library](https://testing-library.com/docs/react-testing-library/intro/): A lightweight solution for testing React components.

- [Webpack](https://webpack.js.org/): A module bundler for modern JavaScript applications.

# ClojureScript Technologies

The project also uses several ClojureScript libraries:

- [Shadow-cljs](https://shadow-cljs.github.io/docs/UsersGuide.html): A ClojureScript compiler which seamlessly integrates with JavaScript development tools.

- [Reitit](https://metosin.github.io/reitit/): A fast data-driven router for Clojure(Script).

- [Malli](https://github.com/metosin/malli): A data-driven schema library for Clojure/Script.

- [Promesa](https://funcool.github.io/promesa/latest/): A promise library for Clojure(Script).

- [UIx](https://pitch-io.github.io/uix/): An idiomatic ClojureScript interface to modern React.js.

- [refx](https://github.com/fbeyer/refx): A ClojureScript library for maintaining and manipulating local state in a functional and declarative manner.

Please consult the respective documentation of each dependency to understand its usage and functionality in the project.

## License

This project is licensed under the [MIT License](LICENSE).

# NOTES

## Setting up Amplify

If you have never used Amplify before on the AWS accoutn you wish to use and
do not have profile set up, follow:

https://docs.amplify.aws/cli/start/install/

Otherwise you can start here:

https://docs.amplify.aws/cli/start/workflows/

### Init

```
❯ amplify init
Note: It is recommended to run this command from the root of your app directory
? Enter a name for the project cljstemplate
The following configuration will be applied:

Project information
| Name: cljstemplate
| Environment: dev
| Default editor: Visual Studio Code
| App type: javascript
| Javascript framework: react
| Source Directory Path: src
| Distribution Directory Path: build
| Build Command: npm run-script build
| Start Command: npm run-script start

? Initialize the project with the above configuration? No
? Enter a name for the environment devcljst
? Choose your default editor: Visual Studio Code
✔ Choose the type of app that you're building · javascript
Please tell us about your project
? What javascript framework are you using react
? Source Directory Path:  src/amplify
? Distribution Directory Path: public
? Build Command:  npm run-script build
? Start Command: npm run-script start
Using default provider  awscloudformation
? Select the authentication method you want to use: AWS profile

```

### Add API

```
❯ amplify add api
? Select from one of the below mentioned services: GraphQL
? Here is the GraphQL API that we will create. Select a setting to edit or continue Authorization modes: API key (default, expira
tion time: 7 days from now)
? Choose the default authorization type for the API Amazon Cognito User Pool
Using service: Cognito, provided by: awscloudformation

 The current configured provider is Amazon Cognito.

 Do you want to use the default authentication and security configuration? Default configuration
 Warning: you will not be able to edit these selections.
 How do you want users to be able to sign in? Username
 Do you want to configure advanced settings? No, I am done.
✅ Successfully added auth resource cljstemplatecc274de4 locally

✅ Some next steps:
"amplify push" will build all your local backend resources and provision it in the cloud
"amplify publish" will build all your local backend and frontend resources (if you have hosting category added) and provision it in the cloud

? Configure additional auth types? Yes
? Choose the additional authorization types you want to configure for the API IAM
? Here is the GraphQL API that we will create. Select a setting to edit or continue Conflict detection (required for DataStore):
Disabled
? Enable conflict detection? Yes
? Select the default resolution strategy Auto Merge
? Here is the GraphQL API that we will create. Select a setting to edit or continue Continue
? Choose a schema template: Single object with fields (e.g., “Todo” with ID, name, description)

⚠️  WARNING: Some types do not have authorization rules configured. That means all create, read, update, and delete operations are denied on these types:
         - Todo
Learn more about "@auth" authorization rules here: https://docs.amplify.aws/cli/graphql/authorization-rules
✅ GraphQL schema compiled successfully.

```
