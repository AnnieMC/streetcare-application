import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/api'

export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Fetch all users
export const fetchUsers = async () => {
  try {
    const response = await api.get('/user')
    return response.data
  } catch (error) {
    console.error('Error fetching users:', error)
    throw error
  }
}

// Register user
export const createUser = async (userData) => {
  try {
    const response = await api.post('/user', userData)
    return response.data
  } catch (error) {
    console.error('Error creating user:', error)
    throw error
  }
}

// Log in user
export const loginUser = async (credentials) => {
  try {
    const response = await api.post('/user/login', credentials)
    return response.data
  } catch (error) {
    console.error('Error logging in:', error)
    throw error
  }
}

// Log out user
// export const logoutUser = async () => {
//   try {
//     const response = await api.post('/logout')
//     return response.data
//   } catch (error) {
//     console.error('Error logging out:', error)
//     throw error
//   }
// }
