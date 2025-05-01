<script setup>
import { RouterLink, RouterView } from 'vue-router' //Import router components
import { ref } from 'vue'

const isLoggedIn = ref(false) //Reactive variable to track login status
</script>

<template>
  <header>
    <div class="wrapper">
      <nav>
        <!-- Navigation links -->
        <RouterLink to="/register" v-if="!isLoggedIn">Create Account</RouterLink>
        <RouterLink to="/login" v-if="!isLoggedIn">Log In</RouterLink>
        <RouterLink to="/" v-if="!isLoggedIn">Home</RouterLink>
      </nav>
    </div>
  </header>

  <RouterView />
</template>

<style></style>
