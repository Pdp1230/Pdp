<template>
    <q-page class="bg-grey-2">
      <div class="row justify-center">
        <q-card class="bg-grey-3 q-mt-md col-md-4 col-sm-7 col-xs-10 col-lg-3 col-xl-3">
          <q-card-section class="row justify-center text-bold">
            Login to your account
          </q-card-section>
          <q-card-section class="q-mt-md q-ml-md justify-start">
            <q-input
              type="email"
              placeholder="Email"
              v-model="emailSI"
              flat
              dense
              bordered
            />
            <q-input
              type="password"
              placeholder="Your password"
              flat
              dense
              bordered
              v-model="passwordSI"
            />
          </q-card-section>
          <q-card-section class="row justify-center q-mb-xs">
            <q-btn
              class="full-width"
              label="sign in"
              dense
              color="green"
              @click="triggerLogin"
            />
          </q-card-section>
          <q-card-section class="row justify-center">
            <q-btn
              label="Forgot your password ?"
              dense
              flat
              color="green"
              @click="triggerLogin"
              n-caps
              class="text-underline text-caption"
              :to="{ name: 'ResetPassword' }"
            />
          </q-card-section>
          <q-separator class="q-ml-sm q-mr-sm" color="grey-7" />
          <q-card-section class="row justify-center text-bold">
            Create an account easily
          </q-card-section>
          <q-card-section class="row justify-center">
            <q-btn
              :style="signUpButtonStyle"
              label="Sign up"
              dense
              color="blue"
              @click="addUser"
            />
          </q-card-section>
        </q-card>
        <q-dialog
          v-model="dialogForm"
          transition-show="slide-up"
          transition-hide="slide-down"
        >
          <q-card class="bg-grey-3" :style="signUpStyle">
            <div class="row justify-end">
              <q-btn
                flat
                dense
                round
                color="red"
                icon="close"
                @click="closeDialog"
              />
            </div>
            <q-card-section class="row justify-center text-bold">
              Create an account
            </q-card-section>
            <q-card-section
              class="q-ml-md q-mr-md justify-start"
              v-for="field in fields"
              v-bind:key="field.label"
            >
              <q-input
                :placeholder="'Type in your ' + field.label"
                :type="field.type"
                v-model="field.value"
                flat
                dense
                bordered
              />
            </q-card-section>
            <q-card-section class="row justify-center q-mt-lg q-mb-md">
              <q-btn
                icon="save"
                label="Sign up"
                color="green"
                @click="triggerSignUp"
                :disable="
                  !isFirstNameValid ||
                  !isLastNameValid ||
                  !isEmailValid ||
                  !isPassValid
                "
              />
              <q-tooltip v-if="!isFirstNameValid">
                type a valid first name
              </q-tooltip>
              <q-tooltip v-if="isFirstNameValid && !isLastNameValid">
                type a valid last name
              </q-tooltip>
              <q-tooltip
                v-if="isFirstNameValid && isLastNameValid && !isEmailValid"
              >
                type a valid e-mail
              </q-tooltip>
              <q-tooltip
                v-if="
                  isFirstNameValid &&
                  isLastNameValid &&
                  isEmailValid &&
                  !isPassValid
                "
              >
                password must have at least 8 characters
              </q-tooltip>
            </q-card-section>
          </q-card>
        </q-dialog>
      </div>
    </q-page>
</template>
  
<script>
import api from 'src/api/api';
  export default {
    // eslint-disable-next-line
    name: "Login",
    data() {
      return {
        emailSI: "",
        passwordSI: "",
        fields: [
          { label: "First name", value: "", type: "text" },
          { label: "Last name", value: "", type: "text" },
          { label: "E-mail", value: "", type: "email" },
          { label: "Password", value: "", type: "password" },
        ],
        dialogForm: false,
      };
    },
    computed: {
      cardStyle() {
        return {
          width: "50vh",
          height: "70vh"
        };
      },
      signUpStyle() {
        return {
          minWidth: "60vh",
          maxWidth: "70vh",
          minHeight: "50vh",
          maxHeight: "60hv",
        };
      },
      signUpButtonStyle(){
        return{
          width: "80%"
        }
      },
      isFirstNameValid() {
        return this.fields[0].value.length > 1;
      },
      isLastNameValid() {
        return this.fields[1].value.length > 1;
      },
      isEmailValid() {
        return this.fields[2].value
          .toLowerCase()
          .match(
            /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
          );
      },
      isPassValid() {
        return this.fields[3].value.length > 7;
      },
    },
    methods: {
      addUser() {
        this.dialogForm = true;
      },
      closeDialog() {
        this.dialogForm = false;
        this.fields.forEach((field) => {
          field.value = "";
        });
      },
      async triggerLogin() {
        const data = {
          email: this.emailSI,
          password: this.passwordSI
        };

        try {
          const response = await api.post('/auth/signin', data);
          sessionStorage.setItem('authToken', response.data.token);
          this.$router.push({ name: "Home" });
        } catch (error) {
          console.log(error.message);
        }
      },
      async triggerSignUp() {
        const data = {
          firstname: this.fields[0].value,
          lastname: this.fields[1].value,
          email: this.fields[2].value,
          password: this.fields[3].value
        };

        try {
          const response = await api.post('/auth/signup', data);
          console.log(response.data);
          this.closeDialog();
        } catch (error) {
          console.log(error.message);
        }
      },
    },
  };
</script>