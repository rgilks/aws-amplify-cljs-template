# AWS Amplify ClojureScript Template

This is a ClojureScript project template for AWS Amplify.

By way of an example it sets up the basic functionality to start writing a multi-user realtime game.

## Features

- AWS Amplify integration
- Material UI for styling
- Testing with Karma

## Requirements

This project requires Node.js and npm. The required versions are:

- Node.js: >=16.0.0
- npm: >=8.0.0

## Setting Up a New Project with AWS Amplify and CLJS Template

This is a brief set of instructions to set up a new project, it assumes that you are familiar with this entire document and the structure of the template.

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

1. Navigate to your 'myproject' directory in your terminal and execute `grep -r cljstemplate .`.
2. Rename the `api/cljstemplate` folder to `api/myproject`.
3. Rename the `auth/cljstemplatecc274de4` folder to `api/myprojectc274de4`.
4. Empty the content of the `team-provider-info.json` file.
5. Use a text editor or IDE to find and replace `cljstemplate` with `myproject` across your project, especially check the /amplify.config files. For example, in `project-config.json` change `"projectName": "cljstemplate"` to `"projectName": "myproject"`.
6. Run `amplify init` in your terminal.
7. When asked for the environment name, use something like `devmyprojecta`. This allows you to have multiple environments for the same project in the future, like `devmyprojectb`, `devmyprojectc`, etc.
8. Run `amplify push` and agree to the questions asked.
9. Run `yarn` to install project dependencies.
10. In separate terminals, run `yarn watch`, `yarn webpack`, and `yarn karma`. Ensure all karma tests pass.
11. Navigate to the Amplify Console in your AWS account, go to your app, click on Hosting environments, select GitHub, authorize Amplify to access your 'myproject' repository in GitHub, and connect the main branch.

### 4. Update Package.json and Setup Amplify

1. Change the package name in `package.json` from `aws-amplify-cljs-template` to `myproject`.
2. Run `yarn install` to install project dependencies.
3. Run `amplify init`.
4. Set up the environment using a name like `devmyprojecta`.
5. Select your default editor (e.g., Visual Studio Code) and the authentication method (e.g., AWS profile).
6. Choose the AWS profile you wish to use.
7. Add the backend environment to your AWS Amplify app.
8. Run `amplify push` and answer 'Y' to all questions.
9. Run `amplify add hosting`.
10. Choose the 'Hosting with Amplify Console' option and select 'Continues deployment'.
11. Connect your repository branch and choose the existing environment (e.g., `devmyproject`).
12. Enable 'full-stack continuous deployments (CI/CD)' and use the existing service role `amplifyconsole-backend-role`.
13. Choose the advanced build image (e.g., `public.ecr.aws/n1r2w5d4/tre-amplify-custom-image`).

### 5. Create Test Users and Run Cypress

1. Create test user accounts `testUser1` and `testUser2` with passwords from `cypress-config` in the secrets manager.
2. Run `yarn cypress:run` in your terminal.

### 6. Set Environment Variables

1. Add `CYPRESS_CONFIG` and `BASE_PATH` environment variables in your project or server configuration.

### 7. Setup Rewrites and Redirects

1. Configure any necessary URL rewrites and redirects for your application, as per your project requirements.

## Initial Setup

1. Install Amplify CLI: If you haven't used Amplify before, or don't have a profile set up on the AWS account you plan to use, follow the [Amplify CLI Installation Guide](https://docs.amplify.aws/cli/start/install/).

2. Setup Amplify: Follow the guide on [Amplify CLI Workflows](https://docs.amplify.aws/cli/start/workflows/) to set up Amplify for your project.

## Running the Project

To run the project, you need to start webpack, the development server, and the Karma test runner in separate terminals.

1.  **Start the dev server:**

        yarn watch

2.  **Start webpack:** In a second terminal

        yarn webpack

3.  **Start the Karma test runner:** In a third terminal
    yarn karma

## Cypress Tests

Some of the Cypress tests check that an email is received, for this to work you
need to create some test email accounts and set things up so they can be accessed
by the GMail api.

Create 2 test user email accounts following the instructions for gmail-tester: https://github.com/levz0r/gmail-tester (they seem a bit out of date though), the following is what I did:

1. Copy `cypress-config-example.json` and rename it to `cypress-config.json` update the details in this file as you work though this process
2. Create a new gmail account
3. Log into the account and go to https://console.cloud.google.com/projectselector2/apis/credentials?supportedpurview=project
4. Create a Project (you don't really need to create an organisation)
5. Click `CONFIGURE CONSENT SCREEN`, select User Type `External`, create an app, the details don't matter.
6. When you get to the Test users screen, click `ADD USERS` enter the test email address you are setting up and click `SAVE`
7. Click `SAVE AND CONTINUE`
8. Click `Credentials` in the menu and then `CREATE CREDENTIALS`
9. Select `OAuth client ID`
10. Select Application type: `Desktop app`, add a name if you want
11. Click `CREATE`
12. Click `DOWNLOAD JSON`, put the file in the root of the project for now and rename it `credentials.json`
13. Click `Enabled APIs and services` then `ENABLE APIS AND SERVICES`, search for and select `Gmail API` and click `ENABLE`
14. In a terminal at the root of the project run

```
yarn install

node node_modules/gmail-tester/init.js credentials.json token.json TEST_EMAIL_ADDRESS
```

15. A browser window will open, select the account you with to use.
16. You will see a message like `Google hasn't verified this app`, just click `Continue`, click `Continue` again when asked about access.
17. A token.json file will now appear in the root of the project.
18. Update `cypress-config.json` with the details with the credentials and tokens you just created.

Now we want to store this information in AWS Secrets Manager

1. Got to `https://eu-west-1.console.aws.amazon.com/secretsmanager/listsecrets`
2. Click `Store a new secret`
3. Select `other type of secret`
4. Click plain test and copy the contents of your `cypress-config.json` file there
5. Use the `aws/secretsmanager` encryption key
6. Click `Next`
7. The Secret name is `cypress/config`
8. Click `Next` then `Next` again
9. Click `Store`
10. Check your secret is there and the value is correct

In `cypress-config.json` set `userPoolId` to the `aws_user_pools_id` value in the `aws-exports.js` file.

In the Amplify Console on AWS create an environment variable called `CYPRESS_CONFIG` and copy the contents of `cypress-config.json` into that, for all branches. see: https://docs.aws.amazon.com/amplify/latest/userguide/environment-variables.html

**Delete the files you created while performing this task, i.e credentials.json, token.json and cypress-config.json**

Create accounts in the app for your test users, with user names `testUser1` and `testUser2` with the passwords from the file.

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

## Amplify Build Image

The provided `Dockerfile` is used to construct the build image utilized by the Amplify Console.

`public.ecr.aws/n1r2w5d4/tre-amplify-custom-image` is available publicly, so you can just use that if you like.

To build and deploy your own, create a public repository in ECR, then run the following commands replacing
`n1r2w5d4/tre-amplify-custom-image` with your own registry alias and repository name.

1. Authenticate your Docker client with your registry:

```bash
aws ecr-public get-login-password --region us-east-1 | docker login --username AWS --password-stdin public.ecr.aws/n1r2w5d4
```

1. Build, tag, and push the container to the ECR repository:

```bash
docker buildx build --platform linux/amd64 --push -t public.ecr.aws/n1r2w5d4/tre-amplify-custom-image .
```

By following these instructions, you can update the Docker image in your ECR repository.

## Amplify Console

You need to set a BASE_PATH env var for each subdomain

You also need to set up the following redirects.

![DataStore](/docs/amplify-redirects.png?raw=true)

```
</^(/game)[^.]+$|\.(?!(html|htm|mp3|bin|gltf|css|gif|ico|jpg|js|png|txt|svg|woff|ttf|map|json)$)([^.]+$)/>

</^(/email-settings)[^.]+$|\.(?!(html|htm|mp3|bin|gltf|css|gif|ico|jpg|js|png|txt|svg|woff|ttf|map|json)$)([^.]+$)/>
```

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

## License

This project is licensed under the [MIT License](LICENSE).
