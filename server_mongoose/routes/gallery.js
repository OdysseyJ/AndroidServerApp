// routes/photo.js
module.exports = function(app, Photo)
{
    // GET ALL photos
    app.get('/api/gallery/all', function(req,res){
      Photo.find(function(err, photos){
              if(err) return res.status(500).send({error: 'database failure'});
              res.json(photos);
          })
    });

    // CREATE photo
    app.post('/api/gallery/add', function(req, res){
      var photo = new Photo();
      photo.name = req.body.name;
      photo.photo = req.body.photo;

      //저장시키기
      photo.save(function(err){
          if(err){
              console.error(err);
              res.json({result: 0});
              return;
          }
          res.json({result: 1});
      });
    });

    // DELETE photo
    app.delete('/api/gallery/remove', function(req, res){
      Photo.remove({ _id: req.params.photo_id }, function(err, output){
      if(err) return res.status(500).json({ error: "database failure" });

      /* ( SINCE DELETE OPERATION IS IDEMPOTENT, NO NEED TO SPECIFY )
      if(!output.result.n) return res.status(404).json({ error: "photo not found" });
      res.json({ message: "photo deleted" });
      */

      res.status(204).end();
      })
    });

}
