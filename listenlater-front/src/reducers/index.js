import { combineReducers } from 'redux';
import curUser from './curUser';

const rootReducer = combineReducers({
  curUser,
});

export default rootReducer;