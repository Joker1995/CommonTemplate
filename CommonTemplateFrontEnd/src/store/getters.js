const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  addRouters: state => state.accessPage.addRouters,
  accessPages: state => state.accessPage.accessPages
}
export default getters
