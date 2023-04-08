<template>
  <div class="radio-list-container">
    <div v-for="(choice, index) in options" :key="index">
      <q-radio v-model="selectedChoice" :val="choice" :id="index" />
      <q-label :for="index">{{ choice }}</q-label>
    </div>
  </div>
</template>

<script>
import { ref, watchEffect } from 'vue';

export default {
  props: {
    options: {
      type: Array,
      required: true,
    },
    questionIndex: {
      type: Number,
      required: true,
    },
  },
  setup(props, { emit, attrs }) {
  const selectedChoice = ref(attrs.modelValue || null);

  watchEffect(() => {
    // Emit the selected choice whenever it changes
    emit('update:modelValue', selectedChoice.value);
  });

  return {
    selectedChoice,
  };
},


  // Use the question index as the key to force re-render when the parent component state changes
  key: (props) => `radio-list-${props.questionIndex}`,
};
</script>

<style>
.radio-list-container {
  display: flex;
  flex-direction: row;
  align-items: center;
}   
</style>
