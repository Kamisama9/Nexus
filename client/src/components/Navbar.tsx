import logo from "../assets/Logo.png";
const Navbar = () => {
  return (
    <nav className=" bg-blue-400">
      <div className="flex justify-between items-center mx-auto w-[1440px]"> 
        <div>
          <img src={logo} alt="Logo" className="h-15"></img>
          {/* LOGO */}
          {/* links */}
        </div>
        <div>{/* profile */}</div>
      </div>
    </nav>
  );
};

export default Navbar;
