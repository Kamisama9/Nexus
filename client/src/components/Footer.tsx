import { Film } from "lucide-react";

const Footer = () => {
  return (
    <footer className="border-t border-zinc-800 bg-black">
      <div className="container mx-auto flex max-w-7xl flex-col items-center justify-between gap-6 px-6 py-10 md:flex-row">
        <div className="flex items-center gap-4">
          <div className="flex h-11 w-11 items-center justify-center rounded-2xl bg-gradient-to-br from-amber-400 to-orange-500">
            <Film className="h-5 w-5 text-black" />
          </div>

          <div>
            <h3 className="font-bold text-white">Nexus</h3>

            <p className="text-sm text-zinc-500">
              Personal Local Media Library
            </p>
          </div>
        </div>

        <div className="text-center md:text-right">
          <p className="text-sm text-zinc-500">
            © {new Date().getFullYear()} Nexus
          </p>

          <p className="text-xs text-zinc-600">
            Built with React • Spring Boot • TMDB
          </p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
