<template>
  <q-layout view="lHh Lpr lFf">
    <q-header elevated class="bg-blue-10">
      <q-toolbar>
        <q-btn
          flat
          dense
          round
          icon="menu"
          aria-label="Menu"
          @click="leftDrawerOpen = !leftDrawerOpen"
        />

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
                    <q-item-label header>Bonjour xxx</q-item-label>
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

    <q-drawer v-model="leftDrawerOpen" bordered content-class="bg-grey-1">
      <q-list>
        <q-item-label header class="text-grey-8">
          Essential Links
        </q-item-label>
        <EssentialLink
          v-for="link in essentialLinks"
          :key="link.title"
          v-bind="link"
        />
      </q-list>
    </q-drawer>

    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script>
import EssentialLink from "components/EssentialLink.vue";
import api from 'src/api/api';

const linksData = [
  {
    title: "Docs",
    caption: "quasar.dev",
    icon: "school",
    link: "https://quasar.dev",
  },
  {
    title: "Github",
    caption: "github.com/quasarframework",
    icon: "code",
    link: "https://github.com/quasarframework",
  },
  {
    title: "Discord Chat Channel",
    caption: "chat.quasar.dev",
    icon: "chat",
    link: "https://chat.quasar.dev",
  },
  {
    title: "Forum",
    caption: "forum.quasar.dev",
    icon: "record_voice_over",
    link: "https://forum.quasar.dev",
  },
  {
    title: "Twitter",
    caption: "@quasarframework",
    icon: "rss_feed",
    link: "https://twitter.quasar.dev",
  },
  {
    title: "Facebook",
    caption: "@QuasarFramework",
    icon: "public",
    link: "https://facebook.quasar.dev",
  },
  {
    title: "Quasar Awesome",
    caption: "Community Quasar projects",
    icon: "favorite",
    link: "https://awesome.quasar.dev",
  },
];

export default {
  name: "MainLayout",
  components: {
    EssentialLink,
  },
  data() {
    return {
      leftDrawerOpen: false,
      essentialLinks: linksData,
    };
  },
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
          const response = await api.post('/auth/logout', null, {
            headers: {
              'Authorization': `Bearer ${authToken}`,
              'Content-Type': 'application/json'
            }
          });

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