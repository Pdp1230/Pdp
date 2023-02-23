<script setup lang="ts" >
import 'bootstrap';
import Radiolist from './components/Radiolist.vue'
import TheWelcome from './components/TheWelcome.vue'




// Récupération de tous les éléments de formulaire
var formElements = document.querySelectorAll('input, select');

// Fonction d'envoi de mail
function sendMail() {
  // Récupération des réponses du formulaire
  var answers = {};
  for (var i = 0; i < formElements.length; i++) {
    var element = formElements[i];
    answers[element.name] = element.value;
  }
  
  // Envoi des réponses par email (cette fonction doit être implémentée)
  sendAnswers(answers);
}



function exportData() {
  // Récupération des réponses du formulaire
  const formData = new FormData(document.getElementById("my-big-form"));
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

function importData() {
  const input = document.createElement('input');
  input.type = 'file';
  input.accept = '.json';

  input.onchange = () => {
    const file = input.files[0];
    const reader = new FileReader();
    reader.readAsText(file);

    reader.onload = (e) => {
  try {
      const importedData = JSON.parse(e.target?.result as string);
      const form = document.querySelector("form");

      Object.keys(importedData).forEach((question) => {
        const answer = importedData[question];
        const input = form?.querySelector(`[name="${question}"]`) as HTMLInputElement;
        input.value = answer;
      });
    } catch (error) {
      console.error("Error parsing JSON", error);
    }
  };
  }
  input.click();
}



</script>

<template>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
   
  </head>
  <body>
    <div class="container mt-5">
      <div class="card">
        <div class="card-header">
          Formulaire
        </div>
        <div class="card-body">
          <form id = "my-big-form">
            <div class="form-group">
              <label for="nom">Nom :</label>
              <input type="text" class="form-control" id="nom" name="nom" required>
            </div>
            <div class="form-group">
              <label for="email">Adresse email :</label>
              <input type="email" class="form-control" id="email" placeholder="ismail@example.com" pattern="[^@\s]+@[^@\s]+\.[^@\s]+" title="Invalid email address" name="email" required>
            </div>
            <div class="form-group">
              <label for="question1">Genre :</label>
              <div class="radio-container">
               <Radiolist :choices="['Male', 'Female']"></Radiolist>
              </div>
            </div>
            <div class="form-group">
              <label for="question2">Couleur préférée :</label>
              <select class="form-control" id="question2" name="question2" required>
                <option value="">Choisir...</option>
                <option value="rouge">Rouge</option>
                <option value="bleu">Bleu</option>
              </select>
            </div>
            <input type="submit" class="btn btn-primary" @click="sendMail" value="Envoyer">
          </form>
          <button type="button" @click="exportData">Exporter les réponses</button>
          <button type="button" @click="importData">Importer les réponses</button>

        </div>
      </div>
    </div>
  </body>
</template>

<style scoped>


.container {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.card {
  background-color: #1c1c1c;
  color: #fff;
  border: none;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.4);
  width: 100%;
  transition: transform 0.2s;
  overflow: hidden;
}


.card-header {
  font-size: 2rem;
  text-align: center;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  font-size: 1.5rem;
  margin-bottom: 5px;
}

.form-group input[type='text'],
.form-group input[type='email'] {
  width: 100%;
  padding: 10px;
  font-size: 1.2rem;
  border: none;
  background-color: #fff;
  border-radius: 5px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.btn {
  display: inline-block;
  padding: 10px 20px;
  font-size: 1.2rem;
  border: none;
  background-color: #4CAF50;
  color: #fff;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.btn:hover {
  background-color: #3e8e41;
}
.radio-container div {
  display: inline-block;
  margin-right: 10px;
}

</style>
