# Builds and deploys a new zip file, then updates the lambda function.
# This speeds up development since 'amplify push' can take a long time

# Usage: ./deploy-lambda-fn.sh <function-name> <env> <profile>

# e.g. ./deploy-lambda-fn.sh inviteUsers devcljst tre

cd ../amplify/backend/function/$1/src || exit
rm ../index.zip
yarn pl
yarn test
zip -r ../index.zip index.mjs handler.cljs node_modules
cd ..
aws lambda update-function-code --function-name $1-$2 --zip-file fileb://index.zip --profile $3
