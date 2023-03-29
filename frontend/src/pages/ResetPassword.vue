<template>
    <q-page class="bg-grey-2">
      <div class="row justify-center">
        <q-card class="bg-grey-3 q-mt-md" :style="cardStyle">
          <q-card-section class="row justify-center text-bold">
            Reset your password
          </q-card-section>
          <q-card-section class="q-mt-md q-ml-md justify-start">
            <q-input
              type="email"
              placeholder="E-mail"
              v-model="email"
              flat
              dense
              bordered
            />
          </q-card-section>
          <q-card-section class="row justify-center">
            <q-btn
              label="Reset"
              dense
              color="green"
              @click="triggerForgetPassword"
            />
          </q-card-section>
          <q-card-section class="row justify-center">
            <q-btn
              label="Already have an account ?"
              dense
              flat
              color="green"
              n-caps
              class="text-underline text-caption"
              :to="{ name: 'Login' }"
            />
          </q-card-section>
        </q-card>
      </div>
    </q-page>
</template>
  
<script>
  export default {
    // eslint-disable-next-line
    name: "ResetPassword",
    data() {
      return {
        email: "",
      };
    },
    computed: {
      cardStyle() {
        return {
          minWidth: "60vh",
          maxWidth: "70vh",
        };
      },
    },
    methods: {
      triggerForgetPassword() {},
    },
  };
</script>