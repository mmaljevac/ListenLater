import { LOGIN, LOGOUT } from '../actions';

const initialState = JSON.parse(sessionStorage.getItem('curUser')) || null;

const curUserReducer = (state = initialState, action) => {
  switch (action.type) {
    case LOGIN:
      sessionStorage.setItem('curUser', JSON.stringify(action.payload));
      return action.payload;
    case LOGOUT:
      sessionStorage.removeItem('curUser');
      return null;
    default:
      return state;
  }
};

export default curUserReducer;