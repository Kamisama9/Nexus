import axios from "axios";
import { useEffect, useState } from "react";
import { Folders, FolderSync } from "lucide-react";
import { Link } from "react-router-dom";
import MovieCards from "./MovieCards";
interface videoName {
  id: number;
  fileName: string;
  overView: string;
  posterPath: string;
}


const FileSearch = () => {
  const [videoFiles, setVideoFiles] = useState<videoName[]>();
  const [searchKeyword , setSearchKeyword] = useState<string>("");

  const path = "E:/Movies";
  const handleSync = async () => {
    const res = await axios.post(`http://localhost:8080/api/v1/path`, {
      filePath: `${path}`,
    });
    getAllFiles();
  };

  const getAllFiles = async () => {
    const res = await axios.get("http://localhost:8080/api/v1/all");
    setVideoFiles(res.data);
  };

  const handleSearch = async (keyword: string) =>{
    const res = await axios.get(`http://localhost:8080/api/v1/search/${keyword}`);
    setVideoFiles(res.data); 
  }

  useEffect(() => {
    getAllFiles();
  }, []);

  console.log(videoFiles);

  return (
    <>
      <div>
        <div>
          <input
            type="text"
            placeholder="Search for a file"
            className="p-2 m-2 border border-gray-300 rounded"
            value = {searchKeyword}
            onChange = {(e) => setSearchKeyword(e.target.value)}
          />
          <button onClick={() => handleSearch(searchKeyword)}>
            Search
          </button>
        </div>
        <div>
          {/* pass the video file to movie card component and render it there */}
          <MovieCards videoFiles={videoFiles}/>
          {/* {videoFiles ? (
            <ul>
              {videoFiles.map((item) => {
                return (
                  <Link
                    to={`/play/${item.id}`}
                    key={item.id}
                    className="p-5 m-2"
                  >
                    {item.fileName} 
                  </Link>
                );
              })}
            </ul>
          ) : (
            <p>Loading ....</p>
          )} */}
        </div>
        <button onClick={handleSync} className="p-2 m-2 border border-gray-300 rounded cursor-pointer">
          <FolderSync />
        </button>
      </div>
    </>
  );
};
export default FileSearch;
