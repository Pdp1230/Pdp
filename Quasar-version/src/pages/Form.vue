<template>
    <q-page>
      <q-card>
        <q-card-section>
          <h2 class="text-h4">Formulaire</h2>
        </q-card-section>
        <q-card-section>
          <q-form @submit="sendMail" ref="form" id="my-big-form">
            <q-input v-model="nom" label="Nom :" required />
            <q-input v-model="email" label="Adresse email :" type="email" required />
            <q-formgroup>
            <q-label for="Question1">Genre :</q-label>
           <div class="radio-container"><radiolist :choices="genderOptions" v-model="selectedGender" /></div>
            </q-formgroup>
            <q-select v-model="couleur" label="Couleur préférée :" :options="['Rouge', 'Bleu']" required />
            <q-card-actions>
              <q-btn type="submit" label="Envoyer" color="primary" />
              <q-btn label="Exporter les réponses" color="primary" @click="exportData" />
              <q-btn label="Importer les réponses" color="primary" @click="importData" />
            </q-card-actions>
          </q-form>
        </q-card-section>
      </q-card>
    </q-page>
  </template>
  

  <script>
 import radiolist from "./RadioList.vue";
  
  export default {
    
    components: {
      radiolist
    },
    data() {
      return {
        name: '',
        email: '',
        department: '',
        message: '',
        genderOptions: ["Male", "Female"],
        selectedGender: null,
      };
    },
    methods: {
      sendMail() {
        const answers = {
          name: this.name,
          email: this.email,
          message: this.message,
        };
        this.sendAnswers(answers);
      },
      exportData() {
      // Récupération des réponses du formulaire
      const formData = new FormData(this.$refs.myForm);
      const data = {};
      
      // Boucle sur les entrées du formulaire pour les ajouter à l'objet JSON
      for (const [key, value] of formData.entries()) {
        data[key] = value;
      }
      
      // Conversion de l'objet JSON en chaîne de caractères
      const jsonData = JSON.stringify(data);
      
      // Exportation des données en format JSON
      const a = document.createElement("a");
      const blob = new Blob([jsonData], { type: "application/json" });
      a.href = URL.createObjectURL(blob);
      a.download = "form-data.json";
      a.click();
    }
    ,
      importData() {
        const input = document.createElement('input');
        input.type = 'file';
        input.accept = '.json';
        input.onchange = () => {
          const file = input.files[0];
          const reader = new FileReader();
          reader.readAsText(file);
          reader.onload = (e) => {
            try {
              const importedData = JSON.parse(e.target?.result);
              this.name = importedData.name;
              this.email = importedData.email;
              this.department = importedData.department;
              this.message = importedData.message;
            } catch (error) {
              console.error('Error parsing JSON', error);
            }
          };
        };
        input.click();
      },
      sendAnswers(answers) {
        // Send email implementation
      },
    },
  };
  </script>