import { Play } from "lucide-react";
import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";

interface videoName {
  id: number;
  fileName: string;
  overView: string;
  posterPath: string;
}

const MovieCards = ({
  videoFiles,
}: {
  videoFiles: videoName[] | undefined;
}) => {
  if (!videoFiles || videoFiles.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center rounded-3xl border border-zinc-800 bg-zinc-900/50 py-24 text-center">
        <h2 className="text-2xl font-bold text-white">Nothing Found</h2>

        <p className="mt-3 max-w-md text-zinc-500">
          Try syncing your library or searching with another title.
        </p>
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 gap-8 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
      {videoFiles.map((video) => (
        <div
          key={video.id}
          className="
            group
            overflow-hidden
            rounded-3xl
            bg-zinc-900
            shadow-xl
            shadow-black/40
            transition-all
            duration-300
            hover:-translate-y-2
            hover:shadow-2xl
            hover:shadow-amber-500/10
          "
        >
          {/* Poster */}
            <Link
                to={`/play/${video.id}`}
                className="flex items-center justify-center gap-2"
              >
              </Link>

          <div className="relative overflow-hidden">
            <img
              src={video.posterPath}
              alt={video.fileName}
              loading="lazy"
              className="
                aspect-[2/3]
                w-full
                object-cover
                transition-transform
                duration-500
                group-hover:scale-110
              "
            > </img>

            {/* Gradient */}

            <div className="absolute inset-0 bg-gradient-to-t from-black via-black/10 to-transparent" />

            {/* Play Overlay */}

            <Link  to={`/play/${video.id}`}
              className="
                absolute
                inset-0
                flex
                items-center
                justify-center
                opacity-0
                transition-all
                duration-300
                group-hover:opacity-100
              "
            >
              <div className="rounded-full bg-amber-400 p-5 shadow-lg shadow-amber-500/30">
                <Play className="h-7 w-7 text-black" fill="black" />
              
              </div>
            </Link>
          </div>

          {/* Content */}

          <div className="space-y-4 p-5">
            <div>
              <h3 className="line-clamp-1 text-lg font-bold text-white">
                {video.fileName}
              </h3>

              {video.overView && (
                <p className="mt-2 line-clamp-3 text-sm leading-6 text-zinc-400">
                  {video.overView}
                </p>
              )}
            </div>

            <Button
              asChild
              className="
                h-11
                w-full
                rounded-xl
                bg-amber-400
                font-semibold
                text-black
                hover:bg-amber-300
              "
            >
              <Link
                to={`/play/${video.id}`}
                className="flex items-center justify-center gap-2"
              >
                <Play className="h-4 w-4" fill="black" />
                Play Movie
              </Link>
            </Button>
          </div>
        </div>
      ))}
    </div>
  );
};

export default MovieCards;
