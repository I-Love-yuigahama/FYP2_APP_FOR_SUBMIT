import React, {  useCallback, useEffect, useState} from 'react';
import './UserList.css';
import UserService from '../../service/UserService';
import UpdateUser from '../../adminpage/UpdateUser/UpdateUser';
import CreateUser from '../../adminpage/CreateUser/CreateUser';

export default function UserList() {

  const [users, setusers] = useState([])
  const [selectedUserId, setSelectedUserId] = useState(null)
  const [showCreate, setShowCreate] = useState(false)
  const [error, setError] = useState('')

  const token = sessionStorage.getItem('token')

  useEffect(() => {
    if (!token) return

      const fetchData = async () => {
        try {
          const getUsers = await UserService.getAllUsers(token)
          setusers(getUsers.userDTOList || [])
        } catch (err) {
          setError(err.message)
        }
      }

    fetchData()
  }, [token])

  const fetchData = useCallback(async () => {
    try {
      const getUsers = await UserService.getAllUsers(token)
      setusers(getUsers.userDTOList || [])
    } catch (err) {
      setError(err.message)
    }
  }, [token])


  const handleDelete = async (userId) => {
    try {
      await UserService.deleteUser(userId, token)
      setusers(users.filter(u => u.id !== userId))
    } catch (err) {
      setError(err.message)
    }
  }

  if (showCreate) {
    return <CreateUser token={token} onDone={() => { setShowCreate(false); fetchData() }} />
  }

  if (selectedUserId) {
    return (
      <UpdateUser
        user_id={selectedUserId}
        token={token}
        onDone={() => { setSelectedUserId(null); fetchData() }}
      />
    )
  }

  return (
    <div className="user-page-container">
      <div className="user-content-wrapper">
        <div className="data-card">

          {error && <p style={{ color: 'red' }}>{error}</p>}

          <div className="user-table-header">
            <div className="header-cell">
              <span className="underline-title">UserName</span>
            </div>
            <div className="header-cell right-align">
              <button className="btn-add" onClick={() => setShowCreate(true)}>ADD</button>
            </div>
          </div>

          <div className="user-table-body">
            {users.map((user) => (
              <div className="user-table-row" key={user.id}>
                <div className="col-username">
                  <p>{user.email}</p>
                </div>
                <div className="col-actions">
                  <button className="btn-edit" onClick={() => setSelectedUserId(user.id)}>EDIT</button>
                  <button className="btn-delete" onClick={() => handleDelete(user.id)}>DELETE</button>
                </div>
              </div>
            ))}
          </div>

        </div>
      </div>
    </div>
  )
}