import { useParams, useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { ArrowLeft, Film } from "lucide-react";
import { useEffect, useState } from "react";

const PlayVideo = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [error, setError] = useState(false);

  const videoUrl = `http://localhost:8080/api/v1/play/${id}`;

  useEffect(() => {
    setError(false);
  }, [id]);

  return (
    <div className="min-h-screen bg-zinc-950">

      {/* Header */}

      <header className="sticky top-0 z-50 border-b border-zinc-800 bg-black/60 backdrop-blur-xl">

        <div className="container mx-auto flex h-20 max-w-7xl items-center justify-between px-6">

          <div className="flex items-center gap-4">

            <Button
              variant="ghost"
              size="icon"
              onClick={() => navigate(-1)}
              className="rounded-full bg-zinc-900 hover:bg-zinc-800"
            >
              <ArrowLeft className="h-5 w-5 text-white" />
            </Button>

            <div className="flex h-11 w-11 items-center justify-center rounded-2xl bg-gradient-to-br from-amber-400 to-orange-500">

              <Film className="h-5 w-5 text-black" />

            </div>

            <div>

              <h2 className="font-bold text-white">
                Now Playing
              </h2>

              <p className="text-sm text-zinc-500">
                Movie #{id}
              </p>

            </div>

          </div>

        </div>

      </header>

      <main className="container mx-auto max-w-7xl px-6 py-12">

        {error ? (
          <div className="flex flex-col items-center justify-center rounded-3xl border border-zinc-800 bg-zinc-900 py-24">

            <h2 className="text-3xl font-bold text-white">
              Unable to play video
            </h2>

            <p className="mt-3 text-zinc-500">
              The file is unavailable or cannot be streamed.
            </p>

            <Button
              className="mt-8 bg-amber-400 text-black hover:bg-amber-300"
              onClick={() => navigate("/")}
            >
              Back to Library
            </Button>

          </div>
        ) : (
          <div className="overflow-hidden rounded-3xl border border-zinc-800 bg-black shadow-2xl shadow-black/60">

            <video
              controls
              autoPlay
              className="aspect-video w-full"
              onError={() => setError(true)}
            >
              <source src={videoUrl} type="video/mp4" />
            </video>

          </div>
        )}

      </main>

    </div>
  );
};

export default PlayVideo;