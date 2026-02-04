import FileSearch from "../../components/FIleSearch"
import Navbar from "../../components/Navbar";

const Home = () =>{

    return ( 
        <>
        <Navbar></Navbar>
        <h1 className="text-xl">HI! its Home Page</h1>
        <FileSearch/>
        </>
    )
}

export default Home;