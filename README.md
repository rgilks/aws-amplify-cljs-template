# AWS AMPLIFY CLJS TEMPLATE

This is an early work in progress...

The idea is to have have a good starting point I can use for personal projects
using my favorite technologies.

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

Run the following in separate terminals

```
yarn webpack
```

The webpack terminal will display some errors until the builds run by the following
command are complete.

```
yarn watch
```

```
yarn karma
```

## DataStore

![DataStore](/docs/datastore.webp?raw=true)
