<template>
  <file-upload-and-preview :files="files" :file-domain="fileDomain" :disabled="disabled" :type="2"></file-upload-and-preview>
</template>

<script>
  export default {
    props: ['files', 'fileDomain', 'disabled'],
  }
</script>
