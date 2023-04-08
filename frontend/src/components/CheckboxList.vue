<template>
  <div class="checkbox-list-container">
    <div v-for="(option, index) in options" :key="index">
      <q-checkbox v-model="selectedOptions" :val="option" :id="'checkbox-' + index" />
      <q-label :for="'checkbox-' + index">{{ option }}</q-label>
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
  },
  setup(props, { emit, attrs }) {
  const selectedOptions = ref(attrs.modelValue || []);

  watchEffect(() => {
    // Emit the selected options whenever they change
    emit('update:modelValue', selectedOptions.value);
  });

  return {
    selectedOptions,
  };
},

};
</script>

<style>
.checkbox-list-container {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
</style>
