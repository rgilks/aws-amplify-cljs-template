# This script..
# removes node modules and re-installs them
# Will update the dependencies in package.json if you uncomment ncu -u

# NOTE
# We need to keep the following...


ncu -u
rm -rf node_modules
rm yarn.lock
rm package-lock.json
yarn install
#cd amplify/backend/function/common/lib/nodejs
#ncu -u
#rm -rf node_modules
#rm yarn.lock
#rm package-lock.json
#yarn install
