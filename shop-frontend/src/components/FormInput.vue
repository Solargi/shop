<script setup>
import { inject } from "vue";
import useOnlyNumericInput from '@/composables/useNumericInput';
const {keysOnlyNumbers, pasteOnlyNumbers} = useOnlyNumericInput();
//allows to bring child value to parent so that when it changes in child it will change in parent as well
const modelValue = defineModel();
const props = defineProps({
  id: String,
  required: Boolean,
  invalid: Boolean,
  description: String,
  type: String,
  options: Array,
  onlyNumbers : Boolean,
});
// console.log("only numbers value: " + props.onlyNumbers)
// console.log("function: " + pasteOnlyNumbers)

//gets field proprety from parent object if nested
//from provide in parent
//it will inject field is it exist otherwise it will use
//it's props
const field = inject("field", props);
</script>
<template>
  <!-- this doesn't work as v-model can't be used directly with component -->
  <!-- <component
    :type = "type"
    :is = "type === 'select' ? 'select' : 'input'"
    :id="field.id"
    v-model="modelValue"
    :required="field.required"
    :class="[
      'p-3.5 border-2 border-gray-300 text-gray-900 outline-none w-full rounded-md shadow-sm focus:ring-2 focus:ring-blue-500 ',
      field.invalid ? 'border-red-500' : 'border-gray-300',
    ]"
  >
    <option v-if="type === 'select'" v-for="(option, index) in options" :key="index" :value="option">
      {{ option }}
    </option>
  </component> -->
  <select
    v-if="type === 'select'"
    :id="field.id"
    v-model="modelValue"
    :required="field.required"
    :class="[
      'p-3.5 border-2 border-gray-300 text-gray-900 outline-none w-full rounded-md shadow-sm focus:ring-2 focus:ring-blue-500 ',
      field.invalid ? 'border-red-500' : 'border-gray-300',
    ]"
  >
    <option v-for="(option, index) in options" :key="index" :value="option">
      {{ option }}
    </option>
  </select>
  <input
    v-else
    :id="field.id"
    v-model="modelValue"
    @keydown="onlyNumbers? keysOnlyNumbers($event) : null"
    @paste="onlyNumbers? pasteOnlyNumbers($event) : null"
    :required="field.required"
    :type="type"
    :class="[
      'p-3.5 border-2 border-gray-300 text-gray-900 outline-none w-full rounded-md shadow-sm focus:ring-2 focus:ring-blue-500 ',
      field.invalid ? 'border-red-500' : 'border-gray-300',
    ]"
  />
</template>
