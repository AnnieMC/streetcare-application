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
  <div class="register-container">
    <h2>Create Account</h2>

    <form @submit.prevent="handleRegister" class="register-form">
      <div class="form-group">
        <label class="visually-hidden">Name</label>
        <input v-model="form.name" type="text" placeholder="Name" required />
      </div>

      <div class="form-group">
        <label class="visually-hidden">Email</label>
        <input v-model="form.email" type="email" placeholder="Email" required />
      </div>

      <div class="form-group">
        <label class="visually-hidden">Password</label>
        <input v-model="form.password" type="password" placeholder="Password" required />
      </div>

      <button type="submit">Register</button>

      <p v-if="error" class="error-message">{{ error }}</p>
      <p v-if="success" class="success-message">{{ success }}</p>
    </form>

    <div class="login-link">
      <p>
        Already have an account?
        <RouterLink to="/login">Log In</RouterLink>
      </p>
    </div>
  </div>
</template>

<style>
.register-container {
  max-width: 400px;
  margin: 5em auto;
  padding: 2em;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  text-align: left;
}

.register-container h2 {
  text-align: center;
  margin-bottom: 1em;
}

.register-form .form-group {
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

.register-form input {
  width: 100%;
  padding: 0.75em;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 1em;
}

.register-form input::placeholder {
  color: #888;
}

.register-form button {
  width: 100%;
  padding: 0.75em;
  background-color: #2f855a;
  color: white;
  border: none;
  border-radius: 5px;
  font-weight: bold;
  cursor: pointer;
}

.register-form button:hover {
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

.login-link {
  text-align: center;
  margin-top: 1.5em;
}

.login-link a {
  color: #3182ce;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>
