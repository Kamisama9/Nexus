import axios from "axios";
import { useEffect, useState } from "react";
import { FolderSync, Loader2, Search } from "lucide-react";
import MovieCards from "./MovieCards";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";

interface videoName {
  id: number;
  fileName: string;
  overView: string;
  posterPath: string;
}

const API_BASE = "http://localhost:8080/api/v1";

const FileSearch = () => {
  const [videoFiles, setVideoFiles] = useState<videoName[]>();
  const [searchKeyword, setSearchKeyword] = useState("");
  const [loading, setLoading] = useState(false);
  const [syncing, setSyncing] = useState(false);

  const getAllFiles = async () => {
    setLoading(true);
    try {
      const res = await axios.get(`${API_BASE}/all`);
      setVideoFiles(res.data);
    } finally {
      setLoading(false);
    }
  };

  const handleSync = async () => {
    setSyncing(true);
    try {
      await axios.post(`${API_BASE}/path`, { filePath: "E:/Movies" });
      await getAllFiles();
    } finally {
      setSyncing(false);
    }
  };

  const handleSearch = async () => {
    if (!searchKeyword.trim()) {
      await getAllFiles();
      return;
    }
    setLoading(true);
    try {
      const res = await axios.get(`${API_BASE}/search/${searchKeyword}`);
      setVideoFiles(res.data);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    getAllFiles();
  }, []);

  return (
    <section className="container relative mx-auto -mt-16 max-w-7xl px-6 pb-20">
      {/* Floating Search Panel */}

      <div className="mb-14 rounded-3xl border border-zinc-800 bg-zinc-900/70 p-8 shadow-2xl shadow-black/50 backdrop-blur-xl">
        <div className="mb-8 flex flex-col gap-3">
          <span className="text-sm font-medium uppercase tracking-[0.25em] text-amber-400">
            Library
          </span>

          <h2 className="text-4xl font-black tracking-tight text-white">
            Browse your collection
          </h2>

          <p className="max-w-2xl text-zinc-400">
            Instantly search your local media library and launch movies with a
            single click.
          </p>
        </div>

        <div className="flex flex-col gap-4 lg:flex-row lg:items-center">
          {/* Search */}

          <div className="relative flex-1">
            <Search className="absolute left-5 top-1/2 h-5 w-5 -translate-y-1/2 text-zinc-500" />

            <Input
              placeholder="Search movies..."
              value={searchKeyword}
              onChange={(e) => setSearchKeyword(e.target.value)}
              onKeyDown={(e) => e.key === "Enter" && handleSearch()}
              className="
              h-14
              rounded-full
              border-zinc-700
              bg-zinc-950
              pl-14
              text-base
              text-white
              placeholder:text-zinc-500
              focus-visible:border-amber-400
              focus-visible:ring-amber-400/20
            "
            />
          </div>

          {/* Search Button */}

          <Button
            onClick={handleSearch}
            className="
            h-14
            rounded-full
            bg-amber-400
            px-8
            text-black
            hover:bg-amber-300
          "
          >
            <Search className="mr-2 h-4 w-4" />
            Search
          </Button>

          {/* Sync */}

          <Button
            onClick={handleSync}
            disabled={syncing}
            variant="outline"
            className="
            h-14
            rounded-full
            border-zinc-700
            bg-zinc-950
            px-8
            text-zinc-200
            hover:bg-zinc-800
          "
          >
            {syncing ? (
              <Loader2 className="mr-2 h-4 w-4 animate-spin" />
            ) : (
              <FolderSync className="mr-2 h-4 w-4 text-amber-400" />
            )}
            Sync Library
          </Button>
        </div>
      </div>

      {/* Section Header */}

      {!loading && (
        <div className="mb-8 flex items-center justify-between">
          <div>
            <h3 className="text-3xl font-bold text-white">Your Movies</h3>

            <p className="mt-1 text-zinc-500">
              {videoFiles?.length ?? 0} titles available
            </p>
          </div>
        </div>
      )}

      {/* Loading */}

      {loading && (
        <div className="flex h-80 items-center justify-center">
          <Loader2 className="h-10 w-10 animate-spin text-amber-400" />
        </div>
      )}

      {/* Grid */}

      {!loading && <MovieCards videoFiles={videoFiles} />}
    </section>
  );
};

export default FileSearch;
