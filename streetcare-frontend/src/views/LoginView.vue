<script setup>
import { ref } from 'vue'
import { loginUser } from '@/services/authService'
import { useRouter } from 'vue-router'

const router = useRouter()

const form = ref({
  email: '',
  password: '',
})

const error = ref('')
const success = ref('')

const handleLogin = async () => {
  error.value = ''
  success.value = ''
  try {
    //const response = await loginUser(form.value)
    await loginUser(form.value)

    success.value = 'Login successful!'
    // Save token or user data if needed
    //localStorage.setItem('token', response.token)
    //localStorage.setItem('user', JSON.stringify(response.user))
    // Navigate to home or dashboard
    router.push('/')
  } catch (err) {
    error.value = 'Invalid email or password.'
    console.error(err)
  }
}
</script>

<template>
  <div class="max-w-md mx-auto mt-10 p-6 border rounded shadow bg-white">
    <h2 class="text-xl font-bold mb-4">Log In</h2>

    <form @submit.prevent="handleLogin">
      <div class="mb-4">
        <label class="block text-sm font-medium mb-1">Email</label>
        <input v-model="form.email" type="email" class="w-full p-2 border rounded" required />
      </div>

      <div class="mb-6">
        <label class="block text-sm font-medium mb-1">Password</label>
        <input v-model="form.password" type="password" class="w-full p-2 border rounded" required />
      </div>

      <button type="submit" class="w-full bg-green-600 text-white py-2 rounded hover:bg-green-700">
        Log In
      </button>

      <p v-if="error" class="text-red-500 mt-4">{{ error }}</p>
      <p v-if="success" class="text-green-500 mt-4">{{ success }}</p>
    </form>
  </div>
</template>
