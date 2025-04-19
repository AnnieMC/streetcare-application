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
    console.log('Login response:', response)
    if (response.data) {
      // Save user to localStorage
      localStorage.setItem('user', JSON.stringify(response.data))
      console.log('User saved to localStorage:', JSON.stringify(response.data))

      // Immediately try to get the user after saving it to localStorage
      const userFromStorage = localStorage.getItem('user')
      console.log('User from localStorage immediately after save:', userFromStorage)

      // Also check if JSON.parse works properly
      try {
        const parsedUser = JSON.parse(userFromStorage)
        console.log('Parsed user:', parsedUser)
      } catch (error) {
        console.error('Error parsing user data:', error)
      }
    }
    return response.data
  } catch (error) {
    console.error('Error logging in:', error)
    throw error
  }
}

//get current user
export const getCurrentUser = () => {
  const userStr = localStorage.getItem('user')
  console.log('getCurrentUser - user from localStorage:', userStr)

  if (!userStr || userStr === 'undefined') {
    console.log('No user found or user is undifened')
    return null
  }
  // Check if userStr is a valid JSON string
  try {
    const parsedUser = JSON.parse(userStr)
    console.log('Parsed user in getCurrentUser:', parsedUser)
    return parsedUser
  } catch (error) {
    console.error('Error parsing user data:', error)
    return null
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
