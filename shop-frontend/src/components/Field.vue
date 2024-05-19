<script setup>
import FormLabel from "@/components/FormLabel.vue";
import ErrorMessage from "./ErrorMessage.vue";
import InputDescription from "./InputDescription.vue";
import { v4 as uuid } from "uuid";
import { computed, provide } from "vue";

const props = defineProps({
  id: {
    type: String,
    default: () => `field-${uuid()}}`,
  },
  label: String,
  required: Boolean,
  errorMessage: String,
  description: String,
});
//give id to desc if present
const descriptionFor = computed(() => {
    return !!props.description ? `description-${uuid()}` : null;
})

//provide props to child components
// if they use ineject, to make props read only it's
//better to return them in a computed property
//instead of doing provide('field',props)
provide('field', computed(() =>  {
    return {
        ...props, //copies proprieties of props in the return object
        invalid: !!props.errorMessage,
        descriptionFor: descriptionFor.value,
    };
    })
)
</script>

<template>
  <div>
    <FormLabel v-if="props.label" :for="props.id" :required="props.required">{{
      props.label
    }}</FormLabel>
    <slot />
    <InputDescription v-if="props.description" :id="descriptionFor">
      {{ props.description }}
    </InputDescription>
    <ErrorMessage v-if="props.error">
      {{ props.error }}
    </ErrorMessage>
  </div>
</template>
