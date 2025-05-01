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
    await loginUser(form.value)

    success.value = 'Login successful!'
    router.push('/report')
  } catch (err) {
    error.value = 'Invalid email or password.'
    console.error(err)
  }
}
</script>

<!-- <template>
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

    <div class="mt-4 text-center">
      <p>
        Don't have an account?
        <RouterLink to="/register" class="text-blue-600">Create one</RouterLink>
      </p>
    </div>
  </div>
</template>

<style>
.login-container {
  max-width: 400px;
  margin: 5em auto;
  padding: 2em;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  text-align: left;
}

.login-container h2 {
  text-align: center;
  margin-bottom: 1em;
}

.login-form .form-group {
  margin-bottom: 1.5em;
}

.login-form label {
  display: block;
  margin-bottom: 0.5em;
  font-weight: bold;
}

.login-form input {
  width: 100%;
  padding: 0.75em;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.login-form button {
  width: 100%;
  padding: 0.75em;
  background-color: #2f855a;
  color: white;
  border: none;
  border-radius: 5px;
  font-weight: bold;
  cursor: pointer;
}

.login-form button:hover {
  background-color: #276749;
}

.error-message {
  color: #e53e3e;
  margin-top: 1em;
  text-align: center;
}

.success-message {
  color: #38a169;
  margin-top: 1em;
  text-align: center;
}

.register-link {
  text-align: center;
  margin-top: 1.5em;
}

.register-link a {
  color: #3182ce;
  text-decoration: none;
}

.register-link a:hover {
  text-decoration: underline;
}
</style> -->

<template>
  <div class="login-container">
    <h2>Log In</h2>

    <form @submit.prevent="handleLogin" class="login-form">
      <div class="form-group">
        <label class="visually-hidden">Email</label>
        <input v-model="form.email" type="email" placeholder="Email" required />
      </div>

      <div class="form-group">
        <label class="visually-hidden">Password</label>
        <input v-model="form.password" type="password" placeholder="Password" required />
      </div>

      <button type="submit">Log In</button>

      <p v-if="error" class="error-message">{{ error }}</p>
      <p v-if="success" class="success-message">{{ success }}</p>
    </form>

    <div class="register-link">
      <p>
        Don't have an account?
        <RouterLink to="/register">Create one</RouterLink>
      </p>
    </div>
  </div>
</template>

<style>
.login-container {
  max-width: 400px;
  margin: 5em auto;
  padding: 2em;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  text-align: left;
}

.login-container h2 {
  text-align: center;
  margin-bottom: 1em;
}

.login-form .form-group {
  margin-bottom: 1.5em;
}

.visually-hidden {
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  white-space: nowrap;
  border: 0;
}

.login-form input {
  width: 100%;
  padding: 0.75em;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 1em;
}

.login-form input::placeholder {
  color: #888;
}

.login-form button {
  width: 100%;
  padding: 0.75em;
  background-color: #2f855a;
  color: white;
  border: none;
  border-radius: 5px;
  font-weight: bold;
  cursor: pointer;
}

.login-form button:hover {
  background-color: #276749;
}

.error-message {
  color: #e53e3e;
  margin-top: 1em;
  text-align: center;
}

.success-message {
  color: #38a169;
  margin-top: 1em;
  text-align: center;
}

.register-link {
  text-align: center;
  margin-top: 1.5em;
}

.register-link a {
  color: #3182ce;
  text-decoration: none;
}

.register-link a:hover {
  text-decoration: underline;
}
</style>
