import './App.css'
import PlayVideo from './components/PlayVideo'
import Home from './pages/Home/Home'
import { BrowserRouter as Router , Routes ,Route } from 'react-router-dom'
function App() {

  return (
    <Router>
      <Routes>
        <Route path='/' element={<Home/>}/>
        <Route path='/play/:id' element={<PlayVideo/>}/>
      </Routes>
    </Router>
  )
}

export default App
