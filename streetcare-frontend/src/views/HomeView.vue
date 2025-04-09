<script>
import { ref, onMounted } from 'vue'
import { fetchUsers } from '@/services/authService'

export default {
  setup() {
    const users = ref([])

    const loadUsers = async () => {
      try {
        users.value = await fetchUsers()
      } catch (error) {
        console.error('Error loading users:', error)
      }
    }

    onMounted(loadUsers)

    return { users }
  },
}
</script>

<template>
  <div>
    <h1>Welcome to StreetCare</h1>
    <ul>
      <li v-for="user in users" :key="user.id">{{ user.name }} - {{ user.email }}</li>
    </ul>
  </div>
  <div>
    <h1>User List</h1>
    <ul>
      <li v-for="user in users" :key="user.id">{{ user.name }} - {{ user.email }}</li>
    </ul>
  </div>
</template>
