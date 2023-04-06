<template>
  <q-layout view="lHh Lpr lFf">
    <q-header elevated class="bg-blue-10">
      <q-toolbar>
        <q-toolbar-title class="cursor-pointer" @click="goToHome">
          UBFORMS
        </q-toolbar-title>

        <div v-if="isLoggedIn">
          <q-btn icon="person" flat dense>
            <!--<q-tooltip>this is a test</q-tooltip>-->
            <q-menu>
              <q-card style="min-width: 200px" class="bg-grey-4">
                <q-card-section class="row justify-start">
                  <q-list padding>
                    <q-item-label header>Hello xxx</q-item-label>
                    <q-item class="cursor-pointer">
                      <q-item-section avatar>
                        <q-icon color="primary" name="settings" />
                      </q-item-section>
                      <q-item-section>Settings</q-item-section>
                    </q-item>
                    <q-separator />
                  </q-list>
                </q-card-section>
                <q-card-section class="row justify-center q-mt-md">
                  <q-btn
                    label="logout"
                    icon="logout"
                    color="warning"
                    @click="logout"
                  >
                  </q-btn>
                </q-card-section>
              </q-card>
            </q-menu>
          </q-btn>
        </div>
      </q-toolbar>
    </q-header>

    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script>
import api from 'src/api/api';

export default {
  name: "MainLayout",
  computed: {
    isLoggedIn() {
      return this.$route.name === "Home";
    },
  },
  methods: {
    goToHome() {
      this.$router.push({ name: "Home" });
    },
    async logout() {
      const authToken = sessionStorage.getItem('authToken');

      if (authToken != null) {
        try {
          const response = await api.post('/auth/logout',
            {
              headers: {
                Authorization: `Bearer ${authToken}`,
                'Content-Type': 'application/json'
              }
            }
          );

          if (response.status !== 200) {
            throw new Error('Logout failed.');
          } else {
            sessionStorage.removeItem('authToken');
          }
        } catch (error) {
            console.log(error.message);
        }
      }
      this.$router.push({ name: "Login" });
    }
  },
};
</script>