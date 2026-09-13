import { Film, Menu, Moon, Sun, X } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useState } from "react";
import { Link } from "react-router-dom";

const Navbar = () => {
  const [isDark, setIsDark] = useState(true);
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  const toggleDark = () => {
    setIsDark(!isDark);
    document.documentElement.classList.toggle("dark");
  };

  return (
    <header className="sticky top-0 z-50">
      {/* Glass Background */}
      <div className="absolute inset-0 border-b border-zinc-800/70 bg-black/40 backdrop-blur-2xl" />

      <div className="relative container mx-auto flex h-20 max-w-7xl items-center justify-between px-6">
        {/* Logo */}

        <Link to="/" className="group flex items-center gap-3">
          <div className="flex h-11 w-11 items-center justify-center rounded-2xl bg-gradient-to-br from-amber-400 to-orange-500 shadow-lg shadow-amber-500/20 transition-transform duration-300 group-hover:scale-105">
            <Film className="h-5 w-5 text-black" />
          </div>

          <div>
            <h1 className="text-xl font-black tracking-tight text-white">
              Nexus
            </h1>

            <p className="text-xs text-zinc-500">Local Media Library</p>
          </div>
        </Link>

        {/* Desktop */}

        <nav className="hidden items-center gap-3 md:flex">
        </nav>

        {/* Mobile */}

        <div className="flex items-center gap-2 md:hidden">
          
          <Button
            variant="ghost"
            size="icon"
            className="rounded-full bg-zinc-900"
            onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
          >
            {mobileMenuOpen ? (
              <X className="h-5 w-5 text-white" />
            ) : (
              <Menu className="h-5 w-5 text-white" />
            )}
          </Button>
        </div>
      </div>

      {/* Mobile Menu */}

      {mobileMenuOpen && (
        <div className="border-b border-zinc-800 bg-black/95 backdrop-blur-xl md:hidden">
          <div className="container mx-auto flex flex-col gap-3 px-6 py-6">
            <Button
              asChild
              variant="ghost"
              className="justify-start rounded-xl text-zinc-300 hover:bg-zinc-900 hover:text-white"
              onClick={() => setMobileMenuOpen(false)}
            >
              <Link to="/">Browse Library</Link>
            </Button>
          </div>
        </div>
      )}
    </header>
  );
};

export default Navbar;
