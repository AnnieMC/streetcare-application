import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/api' //API base URL

//Axios instance for making API requests
export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

//Get all users
export const fetchUsers = async () => {
  try {
    const response = await api.get('/user') //Fetch all users from the server
    return response.data
  } catch (error) {
    console.error('Error fetching users:', error)
    throw error
  }
}

// Register user
export const createUser = async (userData) => {
  try {
    const response = await api.post('/user', userData) //Register a new user by sending data to the server
    return response.data
  } catch (error) {
    console.error('Error creating user:', error)
    throw error
  }
}

// Log in user
export const loginUser = async (credentials) => {
  try {
    const response = await api.post('/user/login', credentials) //Send login credentials to the server
    console.log('Login response:', response)
    if (response.data) {
      // Save authenticated user data to localStorage
      localStorage.setItem('user', JSON.stringify(response.data))
      console.log('User saved to localStorage:', JSON.stringify(response.data))

      // Immediately try to get the user after saving it to localStorage
      const userFromStorage = localStorage.getItem('user')
      console.log('User from localStorage immediately after save:', userFromStorage)

      try {
        const parsedUser = JSON.parse(userFromStorage)
        console.log('Parsed user:', parsedUser) //Check if JSON.parse works properly
      } catch (error) {
        console.error('Error parsing user data:', error)
      }
    }
    return response.data //Return the response data
  } catch (error) {
    console.error('Error logging in:', error)
    throw error
  }
}

//get current user
export function getCurrentUser() {
  const userJson = localStorage.getItem('user') //Get user from localStorage
  if (!userJson) return null

  try {
    const parsed = JSON.parse(userJson)
    console.log('Parsed user in getCurrentUser:', parsed) //Check if JSON.parse works properly
    return parsed
  } catch (err) {
    console.error('Error parsing user from localStorage:', err)
    return null
  }
}
