import axios from "axios";
import { useEffect, useState } from "react";
import { Folders, FolderSync } from "lucide-react";
import { Link } from "react-router-dom";
interface videoName {
  id: number;
  name: string;
}

const FileSearch = () => {
  const [videoFiles, setVideoFiles] = useState<videoName[]>();

  const path = "/home/kami";
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

  useEffect(() => {
    getAllFiles();
  }, []);
  return (
    <>
      <div>
        <div>
          {videoFiles ? (
            <ul>
              {videoFiles.map((item) => {
                return (
                  <Link
                    to={`/play/${item.id}`}
                    key={item.id}
                    className="p-5 m-2"
                  >
                    {item.name}
                  </Link>
                );
              })}
            </ul>
          ) : (
            <p>Loading ....</p>
          )}
        </div>
        <button onClick={handleSync}>
          <FolderSync />
        </button>
      </div>
    </>
  );
};
export default FileSearch;
