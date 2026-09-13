import FileSearch from "@/components/FIleSearch";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import { Play, FolderOpen } from "lucide-react";
import { Button } from "@/components/ui/button";

const Home = () => {
  return (
    <div className="min-h-screen bg-zinc-950 text-white">
      <Navbar />

      <main>
        {/* Hero */}
        <section className="relative overflow-hidden">
          {/* Background */}
          <div className="absolute inset-0 bg-gradient-to-br from-zinc-900 via-black to-zinc-950" />

          {/* Glow */}
          <div className="absolute -left-20 top-10 h-80 w-80 rounded-full bg-amber-400/10 blur-[120px]" />
          <div className="absolute right-0 bottom-0 h-96 w-96 rounded-full bg-orange-500/5 blur-[150px]" />

          <div className="relative container mx-auto max-w-7xl px-6 py-24">
            <div className="grid items-center gap-16 lg:grid-cols-2">
              {/* Left */}

              <div>
                <span className="mb-6 inline-flex rounded-full border border-amber-500/20 bg-amber-500/10 px-4 py-2 text-sm font-medium text-amber-300">
                  Your Personal Streaming Hub
                </span>

                <h1 className="max-w-xl text-6xl font-black leading-tight tracking-tight">
                  Watch your
                  <span className="block text-amber-400">
                    Local Movie Library
                  </span>
                </h1>

                <p className="mt-6 max-w-lg text-lg leading-8 text-zinc-400">
                  Browse, search and instantly stream movies stored on your
                  computer with a beautiful modern interface.
                </p>

                <div className="mt-10 flex flex-wrap gap-4">
                  <Button
                    size="lg"
                    className="rounded-full bg-amber-400 px-7 text-black hover:bg-amber-300"
                  >
                    <Play className="mr-2 h-4 w-4 fill-black" />
                    Browse Library
                  </Button>

                  <Button
                    size="lg"
                    variant="outline"
                    className="rounded-full border-zinc-700 bg-zinc-900/40 hover:bg-zinc-800"
                  >
                    <FolderOpen className="mr-2 h-4 w-4" />
                    Sync Movies
                  </Button>
                </div>
              </div>

              {/* Right */}

              <div className="relative hidden lg:flex justify-center">
                <div className="absolute inset-0 rounded-[32px] bg-amber-500/10 blur-3xl" />

                <img
                  src="https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?q=80&w=1200&auto=format&fit=crop"
                  className="relative h-[520px] w-[360px] rounded-[28px] object-cover shadow-2xl shadow-black/60 ring-1 ring-zinc-800"
                  alt="Movie"
                />
              </div>
            </div>
          </div>
        </section>

        {/* Search + Movies */}

        <section className="relative -mt-12 pb-20">
          <FileSearch />
        </section>
      </main>

      <Footer />
    </div>
  );
};

export default Home;
