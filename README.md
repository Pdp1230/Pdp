# Forms (ubforms)

Forms with premium features

# Table of contents

- [Forms (ubforms)](#forms-ubforms)
  - [Pre Requirements](#pre-requirements)
    - [First step: Install NVM(Node Version Manager)](#first-step-install-nvmnode-version-manager)
    - [Second step: Install Node JS and NPM(Node Package Manager)](#second-step-install-node-js-and-npmnode-package-manager)
    - [Last step: Install Quasar Framework](#last-step-install-quasar-framework)
  - [Install the dependencies](#install-the-dependencies)
  - [Start the app in development mode (hot-code reloading, error reporting, etc.)](#start-the-app-in-development-mode-hot-code-reloading-error-reporting-etc)
  - [Lint the files](#lint-the-files)
    - [Format the files](#format-the-files)
  - [Build the app for production](#build-the-app-for-production)
  - [Customize the configuration](#customize-the-configuration)

## Pre Requirements FrontEnd

### First step: Install NVM(Node Version Manager)

#### Install `nvm` using `curl` or `wget`(both can be installed with sudo apt-get install `curl`/`wget`) :
```bash
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.3/install.sh | bash
```
Or:
```bash
wget -qO- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.3/install.sh | bash
```

#### Then execute the following 2 command lines in order:
```bash
export NVM_DIR="$([ -z "${XDG_CONFIG_HOME-}" ] && printf %s "${HOME}/.nvm" || printf %s "${XDG_CONFIG_HOME}/nvm")"
[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh" # This loads nvm
```

#### Verify installation
```bash
command -v nvm
```
This should output `nvm`

### Second step: Install Node JS and NPM(Node Package Manager)
```bash
nvm install 18.14.2
```
Similar to `nvm install --lts` since `18.14.2` is the latest version of the Node Hydrogen release

#### Verify npm and node versions:
```bash
npm --version
```
```bash
node -v
```

#### Set the installed Node version to default:
```bash
nvm alias default 18.14.2
```
This version will be automatically selected when a new session spawns. 
You can also reference it by the alias like in the following command:
```bash
nvm use default
```

### Last step: Install Quasar Framework
```bash
npm install -g @quasar/cli
```

## Install the dependencies
```bash
yarn
# or
npm install
```

## Start the app in development mode (hot-code reloading, error reporting, etc.)
```bash
quasar dev
```


## Lint the files
```bash
yarn lint
# or
npm run lint
```


### Format the files
```bash
yarn format
# or
npm run format
```



## Build the app for production
```bash
quasar build
```

## Customize the configuration
See [Configuring quasar.config.js](https://v2.quasar.dev/quasar-cli-webpack/quasar-config-js).




## Pre Requirements Backend
### Spring Boot Building Installation API
- JDK 17
- Maven 3.6.3
- MySQL 8

### Launching MySQL
```bash
mysql -u root -p[password]

```
Note: Replace [password] with your actual MySQL password when using the command.


### Build the project:
```bash
mvn clean install
```
### Run the application:
```bash
mvn spring-boot:run
```

The API will now be available at `http://localhost:8081`.