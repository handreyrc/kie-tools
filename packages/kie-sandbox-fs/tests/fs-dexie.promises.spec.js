import KieSandboxFs from "@kie-tools/kie-sandbox-fs";
import DexieBackend from "../src/DexieBackend";
import DefaultBackend from "../src/DefaultBackend";

const fs = new KieSandboxFs("testfs-dexie-promises", {
  wipe: true,
  backend: new DefaultBackend({
    idbBackendDelegate: (fileDbName, fileStoreName) => {
      return new DexieBackend(fileDbName, fileStoreName);
    },
  }),
}).promises;

const HELLO = new Uint8Array([72, 69, 76, 76, 79]);

describe("dexie::fs.promises module", () => {
  describe("dexie::mkdir", () => {
    it("root directory already exists", (done) => {
      fs.mkdir("/").catch((err) => {
        expect(err).not.toBe(null);
        expect(err.code).toEqual("EEXIST");
        done();
      });
    });
    it("create empty directory", (done) => {
      fs.mkdir("/mkdir-test")
        .then(() => {
          fs.stat("/mkdir-test").then((stat) => {
            done();
          });
        })
        .catch((err) => {
          expect(err.code).toEqual("EEXIST");
          done();
        });
    });
  });

  describe("dexie::writeFile", () => {
    it("create file", (done) => {
      fs.mkdir("/writeFile")
        .catch(() => {})
        .finally(() => {
          fs.writeFile("/writeFile/writeFile-uint8.txt", HELLO).then(() => {
            fs.stat("/writeFile/writeFile-uint8.txt").then((stats) => {
              expect(stats.size).toEqual(5);
              done();
            });
          });
        });
    });
    it("create file (from string)", (done) => {
      fs.mkdir("/writeFile")
        .catch(() => {})
        .finally(() => {
          fs.writeFile("/writeFile/writeFile-string.txt", "HELLO").then(() => {
            fs.stat("/writeFile/writeFile-string.txt").then((stats) => {
              expect(stats.size).toEqual(5);
              done();
            });
          });
        });
    });
    it("write file perserves old inode", (done) => {
      fs.mkdir("/writeFile")
        .catch(() => {})
        .finally(() => {
          fs.writeFile("/writeFile/writeFile-inode.txt", "HELLO").then(() => {
            fs.stat("/writeFile/writeFile-inode.txt").then((stats) => {
              let inode = stats.ino;
              fs.writeFile("/writeFile/writeFile-inode.txt", "WORLD").then(() => {
                fs.stat("/writeFile/writeFile-inode.txt").then((stats) => {
                  expect(stats.ino).toEqual(inode);
                  done();
                });
              });
            });
          });
        });
    });
    it("write file perserves old mode", (done) => {
      fs.mkdir("/writeFile")
        .catch(() => {})
        .finally(() => {
          fs.writeFile("/writeFile/writeFile-mode.txt", "HELLO", { mode: 0o635 }).then(() => {
            fs.stat("/writeFile/writeFile-mode.txt").then((stats) => {
              let mode = stats.mode;
              expect(mode).toEqual(0o635);
              fs.writeFile("/writeFile/writeFile-mode.txt", "WORLD").then(() => {
                fs.stat("/writeFile/writeFile-mode.txt").then((stats) => {
                  expect(stats.mode).toEqual(0o635);
                  done();
                });
              });
            });
          });
        });
    });
  });

  describe("dexie::readFile", () => {
    it("read non-existant file throws", (done) => {
      fs.readFile("/readFile/non-existant.txt").catch((err) => {
        expect(err).not.toBe(null);
        done();
      });
    });
    it("read file", (done) => {
      fs.mkdir("/readFile")
        .catch(() => {})
        .finally(() => {
          fs.writeFile("/readFile/readFile-uint8.txt", "HELLO").then(() => {
            fs.readFile("/readFile/readFile-uint8.txt").then((data) => {
              // instanceof comparisons on Uint8Array's retrieved from IDB are broken in Safari Mobile 11.x (source: https://github.com/dfahlander/Dexie.js/issues/656#issuecomment-391866600)
              expect([...data]).toEqual([...HELLO]);
              done();
            });
          });
        });
    });
    it("read file (encoding shorthand)", (done) => {
      fs.mkdir("/readFile")
        .catch(() => {})
        .finally(() => {
          fs.writeFile("/readFile/readFile-encoding-shorthand.txt", "HELLO").then(() => {
            fs.readFile("/readFile/readFile-encoding-shorthand.txt", "utf8").then((data) => {
              expect(data).toEqual("HELLO");
              done();
            });
          });
        });
    });
    it("read file (encoding longhand)", (done) => {
      fs.mkdir("/readFile")
        .catch(() => {})
        .finally(() => {
          fs.writeFile("/readFile/readFile-encoding-longhand.txt", "HELLO").then(() => {
            fs.readFile("/readFile/readFile-encoding-longhand.txt", { encoding: "utf8" }).then((data) => {
              expect(data).toEqual("HELLO");
              done();
            });
          });
        });
    });
  });

  describe("dexie::readdir", () => {
    it("read non-existant dir returns undefined", (done) => {
      fs.readdir("/readdir/non-existant").catch((err) => {
        expect(err).not.toBe(null);
        done();
      });
    });
    it("read root directory", (done) => {
      fs.mkdir("/readdir")
        .catch(() => {})
        .finally(() => {
          fs.readdir("/").then((data) => {
            expect(data.includes("readdir")).toBe(true);
            done();
          });
        });
    });
    it("read child directory", (done) => {
      fs.mkdir("/readdir")
        .catch(() => {})
        .finally(() => {
          fs.writeFile("/readdir/1.txt", "").then(() => {
            fs.readdir("/readdir").then((data) => {
              expect(data).toEqual(["1.txt"]);
              done();
            });
          });
        });
    });
    it("read a file throws", (done) => {
      fs.mkdir("/readdir2")
        .catch(() => {})
        .finally(() => {
          fs.writeFile("/readdir2/not-a-dir", "").then(() => {
            fs.readdir("/readdir2/not-a-dir").catch((err) => {
              expect(err).not.toBe(null);
              expect(err.code).toBe("ENOTDIR");
              done();
            });
          });
        });
    });
  });

  describe("dexie::rmdir", () => {
    it("delete root directory fails", (done) => {
      fs.rmdir("/").catch((err) => {
        expect(err).not.toBe(null);
        expect(err.code).toEqual("ENOTEMPTY");
        done();
      });
    });
    it("delete non-existant directory fails", (done) => {
      fs.rmdir("/rmdir/non-existant").catch((err) => {
        expect(err).not.toBe(null);
        expect(err.code).toEqual("ENOENT");
        done();
      });
    });
    it("delete non-empty directory fails", (done) => {
      fs.mkdir("/rmdir")
        .catch(() => {})
        .finally(() => {
          fs.mkdir("/rmdir/not-empty")
            .catch(() => {})
            .finally(() => {
              fs.writeFile("/rmdir/not-empty/file.txt", "").then(() => {
                fs.rmdir("/rmdir/not-empty").catch((err) => {
                  expect(err).not.toBe(null);
                  expect(err.code).toEqual("ENOTEMPTY");
                  done();
                });
              });
            });
        });
    });
    it("delete empty directory", (done) => {
      fs.mkdir("/rmdir")
        .catch(() => {})
        .finally(() => {
          fs.mkdir("/rmdir/empty")
            .catch(() => {})
            .finally(() => {
              fs.readdir("/rmdir").then((data) => {
                let originalSize = data.length;
                fs.rmdir("/rmdir/empty").then(() => {
                  fs.readdir("/rmdir").then((data) => {
                    expect(data.length === originalSize - 1);
                    expect(data.includes("empty")).toBe(false);
                    done();
                  });
                });
              });
            });
        });
    });
    it("delete a file throws", (done) => {
      fs.mkdir("/rmdir")
        .catch(() => {})
        .finally(() => {
          fs.writeFile("/rmdir/not-a-dir", "").then(() => {
            fs.rmdir("/rmdir/not-a-dir").catch((err) => {
              expect(err).not.toBe(null);
              expect(err.code).toBe("ENOTDIR");
              done();
            });
          });
        });
    });
  });

  describe("dexie::unlink", () => {
    it("create and delete file", (done) => {
      fs.mkdir("/unlink")
        .catch(() => {})
        .finally(() => {
          fs.writeFile("/unlink/file.txt", "").then(() => {
            fs.readdir("/unlink").then((data) => {
              let originalSize = data.length;
              fs.unlink("/unlink/file.txt").then(() => {
                fs.readdir("/unlink").then((data) => {
                  expect(data.length).toBe(originalSize - 1);
                  expect(data.includes("file.txt")).toBe(false);
                  fs.readFile("/unlink/file.txt").catch((err) => {
                    expect(err).not.toBe(null);
                    expect(err.code).toBe("ENOENT");
                    done();
                  });
                });
              });
            });
          });
        });
    });
  });

  describe("dexie::rename", () => {
    it("create and rename file", (done) => {
      fs.mkdir("/rename")
        .catch(() => {})
        .finally(() => {
          fs.writeFile("/rename/a.txt", "").then(() => {
            fs.rename("/rename/a.txt", "/rename/b.txt").then(() => {
              fs.readdir("/rename").then((data) => {
                expect(data.includes("a.txt")).toBe(false);
                expect(data.includes("b.txt")).toBe(true);
                fs.readFile("/rename/a.txt").catch((err) => {
                  expect(err).not.toBe(null);
                  expect(err.code).toBe("ENOENT");
                  fs.readFile("/rename/b.txt", "utf8").then((data) => {
                    expect(data).toBe("");
                    done();
                  });
                });
              });
            });
          });
        });
    });
    it("create and rename directory", (done) => {
      fs.mkdir("/rename")
        .catch(() => {})
        .finally(() => {
          fs.mkdir("/rename/a")
            .catch(() => {})
            .finally(() => {
              fs.writeFile("/rename/a/file.txt", "").then(() => {
                fs.rename("/rename/a", "/rename/b").then(() => {
                  fs.readdir("/rename").then((data) => {
                    expect(data.includes("a")).toBe(false);
                    expect(data.includes("b")).toBe(true);
                    fs.readFile("/rename/a/file.txt").catch((err) => {
                      expect(err).not.toBe(null);
                      expect(err.code).toBe("ENOENT");
                      fs.readFile("/rename/b/file.txt", "utf8").then((data) => {
                        expect(data).toBe("");
                        done();
                      });
                    });
                  });
                });
              });
            });
        });
    });
  });

  describe("dexie::symlink", () => {
    it("symlink a file and read/write to it", (done) => {
      fs.mkdir("/symlink")
        .catch(() => {})
        .finally(() => {
          fs.writeFile("/symlink/a.txt", "hello").then(() => {
            fs.symlink("/symlink/a.txt", "/symlink/b.txt").then(() => {
              fs.readFile("/symlink/b.txt", "utf8").then((data) => {
                expect(data).toBe("hello");
                fs.writeFile("/symlink/b.txt", "world").then(() => {
                  fs.readFile("/symlink/a.txt", "utf8").then((data) => {
                    expect(data).toBe("world");
                    done();
                  });
                });
              });
            });
          });
        });
    });
    it("symlink a file and read/write to it (relative)", (done) => {
      fs.mkdir("/symlink")
        .catch(() => {})
        .finally(() => {
          fs.writeFile("/symlink/a.txt", "hello").then(() => {
            fs.symlink("a.txt", "/symlink/b.txt").then(() => {
              fs.readFile("/symlink/b.txt", "utf8").then((data) => {
                expect(data).toBe("hello");
                fs.writeFile("/symlink/b.txt", "world").then(() => {
                  fs.readFile("/symlink/a.txt", "utf8").then((data) => {
                    expect(data).toBe("world");
                    done();
                  });
                });
              });
            });
          });
        });
    });
    it("symlink a directory and read/write to it", (done) => {
      fs.mkdir("/symlink")
        .catch(() => {})
        .finally(() => {
          fs.mkdir("/symlink/a")
            .catch(() => {})
            .finally(() => {
              fs.writeFile("/symlink/a/file.txt", "data").then(() => {
                fs.symlink("/symlink/a", "/symlink/b").then(() => {
                  fs.readdir("/symlink/b").then((data) => {
                    expect(data.includes("file.txt")).toBe(true);
                    fs.readFile("/symlink/b/file.txt", "utf8").then((data) => {
                      expect(data).toBe("data");
                      fs.writeFile("/symlink/b/file2.txt", "world").then(() => {
                        fs.readFile("/symlink/a/file2.txt", "utf8").then((data) => {
                          expect(data).toBe("world");
                          done();
                        });
                      });
                    });
                  });
                });
              });
            });
        });
    });
    it("symlink a directory and read/write to it (relative)", (done) => {
      fs.mkdir("/symlink")
        .catch(() => {})
        .finally(() => {
          fs.mkdir("/symlink/a")
            .catch(() => {})
            .finally(() => {
              fs.mkdir("/symlink/b")
                .catch(() => {})
                .finally(() => {
                  fs.writeFile("/symlink/a/file.txt", "data").then(() => {
                    fs.symlink("../a", "/symlink/b/c").then(() => {
                      fs.readdir("/symlink/b/c").then((data) => {
                        expect(data.includes("file.txt")).toBe(true);
                        fs.readFile("/symlink/b/c/file.txt", "utf8").then((data) => {
                          expect(data).toBe("data");
                          fs.writeFile("/symlink/b/c/file2.txt", "world").then(() => {
                            fs.readFile("/symlink/a/file2.txt", "utf8").then((data) => {
                              expect(data).toBe("world");
                              done();
                            });
                          });
                        });
                      });
                    });
                  });
                });
            });
        });
    });
    it("unlink doesn't follow symlinks", (done) => {
      fs.mkdir("/symlink")
        .catch(() => {})
        .finally(() => {
          fs.mkdir("/symlink/del")
            .catch(() => {})
            .finally(() => {
              fs.writeFile("/symlink/del/file.txt", "data").then(() => {
                fs.symlink("/symlink/del/file.txt", "/symlink/del/file2.txt").then(() => {
                  fs.readdir("/symlink/del").then((data) => {
                    expect(data.includes("file.txt")).toBe(true);
                    expect(data.includes("file2.txt")).toBe(true);
                    fs.unlink("/symlink/del/file2.txt").then((data) => {
                      fs.readdir("/symlink/del").then((data) => {
                        expect(data.includes("file.txt")).toBe(true);
                        expect(data.includes("file2.txt")).toBe(false);
                        fs.readFile("/symlink/del/file.txt", "utf8").then((data) => {
                          expect(data).toBe("data");
                          done();
                        });
                      });
                    });
                  });
                });
              });
            });
        });
    });
    it("lstat doesn't follow symlinks", (done) => {
      fs.mkdir("/symlink")
        .catch(() => {})
        .finally(() => {
          fs.mkdir("/symlink/lstat")
            .catch(() => {})
            .finally(() => {
              fs.writeFile("/symlink/lstat/file.txt", "data").then(() => {
                fs.symlink("/symlink/lstat/file.txt", "/symlink/lstat/file2.txt").then(() => {
                  fs.stat("/symlink/lstat/file2.txt").then((stat) => {
                    expect(stat.isFile()).toBe(true);
                    expect(stat.isSymbolicLink()).toBe(false);
                    fs.lstat("/symlink/lstat/file2.txt").then((stat) => {
                      expect(stat.isFile()).toBe(false);
                      expect(stat.isSymbolicLink()).toBe(true);
                      done();
                    });
                  });
                });
              });
            });
        });
    });
  });

  describe("dexie::readlink", () => {
    it("readlink returns the target path", (done) => {
      fs.mkdir("/readlink")
        .catch(() => {})
        .finally(() => {
          fs.writeFile("/readlink/a.txt", "hello").then(() => {
            fs.symlink("/readlink/a.txt", "/readlink/b.txt").then(() => {
              fs.readlink("/readlink/b.txt", "utf8").then((data) => {
                expect(data).toBe("/readlink/a.txt");
                done();
              });
            });
          });
        });
    });
    it("readlink operates on paths with symlinks", (done) => {
      fs.mkdir("/readlink")
        .catch(() => {})
        .finally(() => {
          fs.symlink("/readlink", "/readlink/sub").then(() => {
            fs.writeFile("/readlink/c.txt", "hello").then(() => {
              fs.symlink("/readlink/c.txt", "/readlink/d.txt").then(() => {
                fs.readlink("/readlink/sub/d.txt").then((data) => {
                  expect(data).toBe("/readlink/c.txt");
                  done();
                });
              });
            });
          });
        });
    });
  });

  describe("dexie::du", () => {
    it("du returns the total file size of a path", (done) => {
      fs.mkdir("/du")
        .catch(() => {})
        .finally(() => {
          fs.writeFile("/du/a.txt", "hello").then(() => {
            fs.writeFile("/du/b.txt", "hello").then(() => {
              fs.mkdir("/du/sub").then(() => {
                fs.writeFile("/du/sub/a.txt", "hello").then(() => {
                  fs.writeFile("/du/sub/b.txt", "hello").then(() => {
                    fs.du("/du/sub/a.txt").then((size) => {
                      expect(size).toBe(5);
                      fs.du("/du/sub").then((size) => {
                        expect(size).toBe(10);
                        fs.du("/du").then((size) => {
                          expect(size).toBe(20);
                          done();
                        });
                      });
                    });
                  });
                });
              });
            });
          });
        });
    });
  });
});
