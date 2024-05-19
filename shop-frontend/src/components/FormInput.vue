<script setup>
import { inject } from 'vue';

//allows to bing child value to parent so that when it changes in child it will change in parent as well
const modelValue = defineModel();
const props =  defineProps({
    id: String,
    required: Boolean,
    invalid : Boolean,
    description: String
});
//gets field proprety from parent object if nested
//from provide in parent
//it will inject field is it exist otherwise it will use
//it's props
const field = inject('field', props)

</script>
<template>
     <input
        :id="field.id"
        v-model="modelValue"
        :required="field.required"
        :class="['p-3.5 border-2 border-gray-300 text-gray-900 outline-none w-full rounded-md shadow-sm focus:ring-2 focus:ring-blue-500 ', 
        field.invalid ? 'border-red-500' : 'border-gray-300',
        ]"
      />

</template>