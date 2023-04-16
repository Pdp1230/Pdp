<template>
    <div>
      <div
        v-for="(option, index) in options"
        :key="index"
        class="drag-option"
        :class="{
          'dragging': dragging && dragIndex === index,
          'over': hovering === index,
          'sorted': option.sorted,
        }"
        :draggable="draggable"
        @dragstart="onDragStart(index, $event)"
        @dragover="onDragOver(index, $event)"
        @drop="onDrop(index, $event)"
      >
        <div class="drag-handle">
          {{ option.label }}
        </div>
        <div class="rank-box">
          <span class="indicator-number">{{ option.rank }}</span>
        </div>
      </div>
      <q-btn icon="restart_alt" @click="resetOptions"/>
    </div>
  </template>
  
  <script>
  export default {
    name: "DraggableOptionGroup",
    props: {
      options: {
        type: Array,
        required: true,
      },
      value: {
        type: Array,
        default: null,
      },
      draggable: {
        type: String,
        default: "true",
      },
      numberOfOptionsToClassify: {
        type: Number,
        required: true,
      },
    },
    data() {
      return {
        dragging: false,
        dragIndex: null,
        targetIndex: null,
        hovering: null,
        sortedOptions: [],
      };
    },
    computed: {
        isSortedOptionsValid () {
        return this.sortedOptions.length === this.numberOfOptionsToClassify;
      },
    },
    methods: {

        resetOptions() {
      this.options.forEach((option) => {
        option.rank = null;
        option.sorted = false;
      });
      this.sortedOptions = [];
      this.$emit("sorted-options-updated", false);
    },
      onDragStart(index, event) {
        const option = this.options[index];
        this.dragging = true;
        this.dragIndex = index;
        this.draggedQuestion = {
          ...option,
          response: { ...option.response },
        };
  
        event.target.classList.add("dragging");
      },
      onDragOver(index, event) {
        event.preventDefault();
        if (!this.dragging) {
          return;
        }
  
        if (index !== this.dragIndex) {
          this.targetIndex = index;
          this.hovering = true;
        } else {
          this.hovering = false;
        }
  
        if (this.targetIndex === null) {
          this.hovering = false;
        }
        // Add event listener for dragleave event
        event.target.addEventListener("dragleave", () => {
          this.hovering = null;
        });
      },
      onDrop(index, event) {
  event.preventDefault();
  const sourceOption = this.options;
  if (index !== this.dragIndex) {
    sourceOption.splice(
      index,
      0,
      sourceOption.splice(this.dragIndex, 1)[0]
    );
    // Check if dragged option already exists in sortedOptions
    const draggedOption = this.options[index];
    if (!this.sortedOptions.includes(draggedOption)) {
      // Add dragged option to sortedOptions array and set sorted property to true
      draggedOption.sorted = true;
      this.sortedOptions.push(draggedOption);
    }
    this.sortedOptions.sort((a, b) => sourceOption.indexOf(a) - sourceOption.indexOf(b));
    // Update the rank property for each option in the sortedOptions array
    this.sortedOptions.forEach((option, indexo) => {
      option.rank = indexo + 1 ;
    });
    
  }
  this.$emit('sorted-options-updated', this.isSortedOptionsValid);
  this.dragging = false;
  this.dragIndex = null;
  this.hovering = null;
},

    },
  };
  </script>
<style>
.drag-option {
  display: flex;
  align-items: center;
  margin-bottom: 5px;
  background-color: #f8f8f8;
  border: 1px solid #ccc;
  cursor: grab;
  transition: all 0.2s ease-in-out;
}

.drag-option:hover {
  background-color: #e0e0e0;
}

.drag-option.dragging {
  opacity: 0.5;
  cursor: grabbing;
}

.drag-option.over {
  background-color: #cfcfcf;
}

.drag-option.sorted {
  background: linear-gradient(to right, rgba(15, 201, 130, 0.522), white);
}

.drag-handle {
  padding: 10px;
  flex-grow: 1;
}

.rank-box {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 30px;
  height: 30px;
  margin-right: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.indicator-number {
  font-size: 18px;
  font-weight: bold;
  color: #4285f4;
}
</style>