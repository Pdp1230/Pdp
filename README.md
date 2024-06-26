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
After installing MySql, the MySQL server may start automatically after installation. If it does not, you can start it manually by running the following command: 
```bash
sudo systemctl start mysql
```

run:
```bash
sudo mysql -u root
```
Then type in these instructions to set a password for root user:
```bash
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'newpassword';
FLUSH PRIVILEGES;
EXIT;
```
If you choose a password other than 'newpassword' you should update your resources/application.properies file with your password because our backend application needs access to create and update the users database.

Then connect to your MySQL server and create a database with the name users Before running spring-boot:
```bash
mysql -u root -p [password]
```
```bash
CREATE DATABASE users;
```
Note: Replace [password] with your actual MySQL password when using the command. 

Now to set up your email sender adress you may go to resources/application.properies file and replace :
```
spring.mail.username=mymail@gmail.com
spring.mail.password=mygeneratedpassapp
```
Note : you must have two step authentification set up in your gmail adress and generate an pass for application you can follow this guide : https://support.google.com/mail/answer/185833?hl=en
### Build the project:
```bash
mvn clean install
```
### Run the application:
```bash
mvn spring-boot:run
```

The API should be started before the frontend server,it will now be available at `http://localhost:8080`, and the frontend at `http://localhost:8081`
