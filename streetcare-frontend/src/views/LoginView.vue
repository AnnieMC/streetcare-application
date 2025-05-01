<script setup>
import { ref } from 'vue'
import { loginUser } from '@/services/authService'
import { useRouter } from 'vue-router'

const router = useRouter()

//Form data for login
const form = ref({
  email: '',
  password: '',
})

const error = ref('') //Error message to display on failed login
const success = ref('') //Success message on successful login

//Handle user login and redirect if successful
const handleLogin = async () => {
  error.value = ''
  success.value = ''
  try {
    await loginUser(form.value) //Call the login function from authService
    success.value = 'Login successful!'
    router.push('/report')
  } catch (err) {
    error.value = 'Invalid email or password.'
    console.error(err)
  }
}
</script>

<template>
  <!-- Login page -->

  <div class="login-container">
    <h2>Log In</h2>

    <!-- Login form -->
    <form @submit.prevent="handleLogin" class="login-form">
      <div class="form-group">
        <label class="visually-hidden">Email</label>
        <input v-model="form.email" type="email" placeholder="Email" required />
      </div>

      <!-- Password input -->
      <div class="form-group">
        <label class="visually-hidden">Password</label>
        <input v-model="form.password" type="password" placeholder="Password" required />
      </div>

      <button type="submit">Log In</button>

      <!-- Error and success messages -->
      <p v-if="error" class="error-message">{{ error }}</p>
      <p v-if="success" class="success-message">{{ success }}</p>
    </form>

    <!-- Link to register page -->
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
