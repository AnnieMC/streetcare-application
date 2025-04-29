<script setup>
import { ref } from 'vue'
import { createUser } from '@/services/authService'

const form = ref({
  name: '',
  email: '',
  password: '',
})

const error = ref('')
const success = ref('')

const handleRegister = async () => {
  error.value = ''
  success.value = ''
  try {
    await createUser(form.value)
    success.value = 'Account created successfully!'
    form.value = { name: '', email: '', password: '' }
  } catch (err) {
    // error.value = 'Registration failed. Please try again.'
    // console.error(err)
    if (err.response && err.response.data) {
      const serverErrors = err.response.data
      // If it's a string (like "Server Error: ...")
      if (typeof serverErrors === 'string') {
        error.value = serverErrors
      }
      // If it's an object with field errors
      else if (typeof serverErrors === 'object') {
        error.value = Object.values(serverErrors).join(', ')
      }
    } else {
      error.value = 'Registration failed. Please try again.'
    }
    //console.error(err)
  }
}
</script>

<template>
  <div class="max-w-md mx-auto mt-10 p-6 border rounded-xl shadow-lg bg-white">
    <h2 class="text-2xl font-bold mb-4">Create Account</h2>

    <form @submit.prevent="handleRegister">
      <div class="mb-4">
        <label class="block mb-1 text-sm font-medium">Name</label>
        <input v-model="form.name" type="text" class="w-full p-2 border rounded" required />
      </div>

      <div class="mb-4">
        <label class="block mb-1 text-sm font-medium">Email</label>
        <input v-model="form.email" type="email" class="w-full p-2 border rounded" required />
      </div>

      <div class="mb-6">
        <label class="block mb-1 text-sm font-medium">Password</label>
        <input v-model="form.password" type="password" class="w-full p-2 border rounded" required />
      </div>

      <button
        type="submit"
        class="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 transition"
      >
        Register
      </button>

      <p v-if="error" class="text-red-500 mt-4 text-sm">{{ error }}</p>
      <p v-if="success" class="text-green-500 mt-4 text-sm">{{ success }}</p>
    </form>

    <div class="mt-4 text-center">
      <p>
        Already have an account? <RouterLink to="/login" class="text-blue-600">Log In</RouterLink>
      </p>
    </div>
  </div>
</template>
