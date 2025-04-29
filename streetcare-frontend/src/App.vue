<script setup>
import { RouterLink, RouterView } from 'vue-router'
import { ref } from 'vue'

const isLoggedIn = ref(false)
</script>

<template>
  <header>
    <div class="wrapper">
      <nav>
        <RouterLink to="/register" v-if="!isLoggedIn">Create Account</RouterLink>
        <RouterLink to="/login" v-if="!isLoggedIn">Log In</RouterLink>
        <RouterLink to="/" v-if="!isLoggedIn">Home</RouterLink>
      </nav>
    </div>
  </header>

  <RouterView />
</template>

<style scoped></style>
