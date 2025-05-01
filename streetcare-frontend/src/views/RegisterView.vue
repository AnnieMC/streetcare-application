<script setup>
import { ref } from 'vue'
import { createUser } from '@/services/authService'

//Form data for registration
const form = ref({
  name: '',
  email: '',
  password: '',
})

const error = ref('') //Error message to display on failed registration
const success = ref('') //Success message on successful registration

//Registration function
const handleRegister = async () => {
  error.value = ''
  success.value = ''
  try {
    await createUser(form.value) //Call the createUser function from authService
    //If registration is successful,
    success.value = 'Account created successfully!'
    form.value = { name: '', email: '', password: '' } //Reset the form
  } catch (err) {
    // console.error(err)
    if (err.response && err.response.data) {
      const serverErrors = err.response.data
      if (typeof serverErrors === 'string') {
        //Check if the server returns an error message as a string
        error.value = serverErrors
      } else if (typeof serverErrors === 'object') {
        //Check if the server returns error message as an object
        error.value = Object.values(serverErrors).join(', ')
      }
    } else {
      error.value = 'Registration failed. Please try again.' //Display fallback error message for any other issues
    }
    console.error(err)
  }
}
</script>

<template>
  <!-- Registration page -->
  <div class="register-container">
    <h2>Create Account</h2>

    <!-- Registration form -->
    <form @submit.prevent="handleRegister" class="register-form">
      <div class="form-group">
        <label class="visually-hidden">Name</label>
        <input v-model="form.name" type="text" placeholder="Name" required />
      </div>

      <!-- Email input -->
      <div class="form-group">
        <label class="visually-hidden">Email</label>
        <input v-model="form.email" type="email" placeholder="Email" required />
      </div>

      <!-- Password input -->
      <div class="form-group">
        <label class="visually-hidden">Password</label>
        <input v-model="form.password" type="password" placeholder="Password" required />
      </div>

      <button type="submit">Register</button>

      <!-- Error and success messages -->
      <p v-if="error" class="error-message">{{ error }}</p>
      <p v-if="success" class="success-message">{{ success }}</p>
    </form>

    <!-- Link to login page -->
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
