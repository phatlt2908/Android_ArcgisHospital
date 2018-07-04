package com.phatlt.suportmedicalacrgisapi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.ArcGISFeature;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.ArcGISVectorTiledLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.GeoElement;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.DrawStatus;
import com.esri.arcgisruntime.mapping.view.DrawStatusChangedEvent;
import com.esri.arcgisruntime.mapping.view.DrawStatusChangedListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.security.Credential;
import com.esri.arcgisruntime.security.UserCredential;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.tasks.networkanalysis.DirectionManeuver;
import com.esri.arcgisruntime.tasks.networkanalysis.Route;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteParameters;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteResult;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteTask;
import com.esri.arcgisruntime.tasks.networkanalysis.Stop;
import com.github.clans.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private MapView mMapView;
    private ArcGISMap mMap;
    private LocationDisplay mLocationDisplay;
    private int requestCode = 2;
    String[] reqPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION};
    private FloatingActionButton btnLocation, btnSearchHospital;

    private DrawerLayout mDrawerLayout;

    private android.graphics.Point mClickPoint;
    private ArcGISFeature mSelectedArcGISFeature;
    private Callout mCallout;

    private static final String TAG = MainActivity.class.getSimpleName();

    private RouteTask mRouteTask;
    private RouteParameters mRouteParams;
    private Route mRoute;
    private SimpleLineSymbol mRouteSymbol;
    private GraphicsOverlay mGraphicsOverlay;
    private Point mSourcePoint;
    private Point mDestinationPoint;

    private ProgressBar progressBar;

    UserCredential userCredential =
            new UserCredential("phatamao", "let1enphat");

    private ListView lvDirection;
    private TextView txtTotalTime, txtTotalLength;
    private SlidingUpPanelLayout slidingLayout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userCredential.createFromToken(getString(R.string.token), "krLJZ0oK3aWfgjD2");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                        menuItem.setChecked(true);
                        int id = menuItem.getItemId();
                        if (!menuItem.isChecked()) {
                            switch (id) {
                                case R.id.nav_camera:
                                    // Handle the camera action
                                    break;
                                case R.id.nav_street:
                                    mMap.setBasemap(Basemap.createStreets());
                                    break;
                                case R.id.nav_open_street_map:
                                    mMap.setBasemap(Basemap.createOpenStreetMap());
                                    break;
                                case R.id.nav_topographic:
                                    mMap.setBasemap(Basemap.createTopographic());
                                    break;
                                case R.id.nav_imagery:
                                    mMap.setBasemap(Basemap.createImagery());
                                    break;
                                case R.id.nav_feedBack:
                                    Intent intent = new Intent(MainActivity.this, FeedBackActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    MainActivity.this.startActivity(intent);
                            }
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });

        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(@NonNull View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(@NonNull View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );

        checkFirstRunApp();
        addControls();

        ArcGISVectorTiledLayer mVectorTiledLayer = new ArcGISVectorTiledLayer(
                getResources().getString(R.string.navigation_vector));

        Basemap basemap = new Basemap(mVectorTiledLayer);

        mMap = new ArcGISMap(basemap);
        mMap.setMinScale(500000);
        mMap.setMaxScale(2000);
        Viewpoint hcmPoint = new Viewpoint(10.823, 106.6297, 200000);
        mMap.setInitialViewpoint(hcmPoint);

        mCallout = mMapView.getCallout();
        // config service feature table
        ServiceFeatureTable serviceFeatureTable = new ServiceFeatureTable(getResources().getString(R.string.service_url));
        serviceFeatureTable.setFeatureRequestMode(ServiceFeatureTable.FeatureRequestMode.ON_INTERACTION_CACHE);
        final FeatureLayer featureLayer = new FeatureLayer(serviceFeatureTable);
        featureLayer.setSelectionColor(android.graphics.Color.CYAN);
        featureLayer.setSelectionWidth(5);
        mMap.getOperationalLayers().add(featureLayer);

        mMapView.setMap(mMap);

        // set an on touch listener to listen for click events
        mMapView.setOnTouchListener(new DefaultMapViewOnTouchListener(this, mMapView) {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                mClickPoint = new android.graphics.Point((int) e.getX(), (int) e.getY());
                featureLayer.clearSelection();
                mSelectedArcGISFeature = null;
                mCallout.dismiss();
                progressBar.setVisibility(View.VISIBLE);

                final ListenableFuture<IdentifyLayerResult> identifyFuture = mMapView
                        .identifyLayerAsync(featureLayer, mClickPoint, 5, false, 1);
                identifyFuture.addDoneListener(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            IdentifyLayerResult layerResult = identifyFuture.get();
                            List<GeoElement> resultGeoElements = layerResult.getElements();

                            if (resultGeoElements.size() > 0) {
                                if (resultGeoElements.get(0) instanceof ArcGISFeature) {
                                    mSelectedArcGISFeature = (ArcGISFeature) resultGeoElements.get(0);
                                    featureLayer.selectFeature(mSelectedArcGISFeature);
                                    showCallout(mSelectedArcGISFeature);
                                }
                            } else {
                                mCallout.dismiss();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Select feature failed: " + e.getMessage());
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
                return super.onSingleTapConfirmed(e);
            }
        });

        // ProgressBar
        mMapView.addDrawStatusChangedListener(new DrawStatusChangedListener() {
            @Override
            public void drawStatusChanged(DrawStatusChangedEvent drawStatusChangedEvent) {
                if (drawStatusChangedEvent.getDrawStatus() == DrawStatus.IN_PROGRESS) {
                    progressBar.setVisibility(View.VISIBLE);
                    Log.d("drawStatusChanged", "spinner visible");
                } else if (drawStatusChangedEvent.getDrawStatus() == DrawStatus.COMPLETED) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

        mLocationDisplay = mMapView.getLocationDisplay();
        // Listen to changes in the status of the location data source.
        mLocationDisplay.addDataSourceStatusChangedListener(new LocationDisplay.DataSourceStatusChangedListener() {
            @Override
            public void onStatusChanged(LocationDisplay.DataSourceStatusChangedEvent dataSourceStatusChangedEvent) {
                // If LocationDisplay started OK, then continue.
                if (dataSourceStatusChangedEvent.isStarted()) {
                    return;
                }

                // No error is reported, then continue.
                if (dataSourceStatusChangedEvent.getError() == null) {
                    return;
                }

                // If an error is found, handle the failure to start.
                // Check permissions to see if failure may be due to lack of permissions.
                boolean permissionCheck1 = ContextCompat.checkSelfPermission(MainActivity.this, reqPermissions[0]) ==
                        PackageManager.PERMISSION_GRANTED;
                boolean permissionCheck2 = ContextCompat.checkSelfPermission(MainActivity.this, reqPermissions[1]) ==
                        PackageManager.PERMISSION_GRANTED;

                if (!(permissionCheck1 && permissionCheck2)) {
                    // If permissions are not already granted, request permission from the user.
                    ActivityCompat.requestPermissions(MainActivity.this, reqPermissions, requestCode);
                } else {
                    // Report other unknown failure types to the user - for example, location services may not
                    // be enabled on the device.
                    String message = String.format("Error in DataSourceStatusChangedListener: %s", dataSourceStatusChangedEvent
                            .getSource().getLocationDataSource().getError().getMessage());
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }
        });

        mLocationDisplay.startAsync();

        addEvents();
    }

    @SuppressLint("SetTextI18n")
    private void showCallout(final ArcGISFeature data) {
        RelativeLayout calloutLayout = new RelativeLayout(getApplicationContext());
        LayoutInflater inflater = getLayoutInflater();
        View viewCallout = inflater.inflate(R.layout.callout, calloutLayout, false);

        TextView txtName = viewCallout.findViewById(R.id.txtName);
        TextView txtAddress = viewCallout.findViewById(R.id.txtAddress);
        TextView txtPhone = viewCallout.findViewById(R.id.txtPhone);
        TextView txtTime = viewCallout.findViewById(R.id.txtTime);
        TextView txtScale = viewCallout.findViewById(R.id.txtScale);
        TextView txtSpectialist = viewCallout.findViewById(R.id.txtSpectialist);
        RatingBar ratingBarScale = viewCallout.findViewById(R.id.ratingBarScale);
        Button btnGoHere = viewCallout.findViewById(R.id.btnGoHere);

        txtName.setText(data.getAttributes().get("name").toString());
        txtAddress.setText(data.getAttributes().get("address").toString());
        txtPhone.setText(data.getAttributes().get("phone").toString());
        txtTime.setText(data.getAttributes().get("open_time").toString() + " - " + data.getAttributes().get("close_time").toString());

        final Double stopLong = Double.parseDouble(data.getAttributes().get("longitude").toString());
        final Double stopLati = Double.parseDouble(data.getAttributes().get("latitude").toString());

        switch (data.getAttributes().get("scale").toString()) {
            case "1":
                txtScale.setText("Nhỏ");
                ratingBarScale.setRating(1);
                break;
            case "2":
                txtScale.setText("Vừa");
                ratingBarScale.setRating(2);
                break;
            case "3":
                ratingBarScale.setRating(3);
                txtScale.setText("Lớn");
                break;
            default:
                txtScale.setText("Không xác định");
                break;
        }
        txtSpectialist.setText(data.getAttributes().get("spectialist").toString());
        txtPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + data.getAttributes().get("phone").toString())));
            }
        });

        btnGoHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Point st1 = mLocationDisplay.getMapLocation();
                final Point st2 = new Point(stopLong, stopLati, SpatialReferences.getWgs84());
                searchRoute(st1, st2);
            }
        });

        calloutLayout.addView(viewCallout);

        mCallout.setGeoElement(mSelectedArcGISFeature, null);
        mCallout.setContent(calloutLayout);
        mCallout.show();
    }

    private void checkFirstRunApp() {
        SharedPreferences sharedPreferences = getSharedPreferences("firstRun", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("firstrun", true)) {
            // Show tooltip here

            sharedPreferences.edit().putBoolean("firstrun", false).apply();
            Toast.makeText(MainActivity.this, "The first run", Toast.LENGTH_SHORT).show();
        }
    }

    private void addEvents() {
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        setupSymbols();

        btnSearchHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Point st1 = mLocationDisplay.getMapLocation();
                Point st2 = new Point(106.619568, 10.779686, SpatialReferences.getWgs84());
                searchRoute(st1, st2);
            }
        });
    }

    private void getLocation() {
        switch (mLocationDisplay.getAutoPanMode().toString()) {
            case "NAVIGATION":
                mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
                mLocationDisplay.startAsync();
                break;
            case "RECENTER":
                mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
                mLocationDisplay.startAsync();
                break;
            case "COMPASS_NAVIGATION":
                mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);
                mLocationDisplay.startAsync();
                break;
            default:
                mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
                mLocationDisplay.startAsync();
                break;
        }
    }

    private void setupSymbols() {

        mGraphicsOverlay = new GraphicsOverlay();

        //add the overlay to the map view
        mMapView.getGraphicsOverlays().add(mGraphicsOverlay);

        //[DocRef: Name=Picture Marker Symbol Drawable-android, Category=Fundamentals, Topic=Symbols and Renderers]
        //Create a picture marker symbol from an app resource
        Bitmap startIcon = BitmapFactory.decodeResource(MainActivity.this.getResources(),
                R.drawable.ic_source);
        BitmapDrawable startDrawable = new BitmapDrawable(startIcon);
        final PictureMarkerSymbol pinSourceSymbol;
        try {
            pinSourceSymbol = PictureMarkerSymbol.createAsync(startDrawable).get();
            pinSourceSymbol.loadAsync();
            pinSourceSymbol.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                    //add a new graphic as start point
                    mSourcePoint = new Point(-117.15083257944445, 32.741123367963446, SpatialReferences.getWgs84());
                    Graphic pinSourceGraphic = new Graphic(mSourcePoint, pinSourceSymbol);
                    mGraphicsOverlay.getGraphics().add(pinSourceGraphic);
                }
            });
            pinSourceSymbol.setOffsetY(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //[DocRef: END]
        Bitmap endIcon = BitmapFactory.decodeResource(MainActivity.this.getResources(),
                R.drawable.ic_destination);
        BitmapDrawable endDrawable = new BitmapDrawable(endIcon);
        final PictureMarkerSymbol pinDestinationSymbol;
        try {
            pinDestinationSymbol = PictureMarkerSymbol.createAsync(endDrawable).get();
            pinDestinationSymbol.loadAsync();
            pinDestinationSymbol.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                    //add a new graphic as end point
                    mDestinationPoint = new Point(-117.15557279683529, 32.703360305883045, SpatialReferences.getWgs84());
                    Graphic destinationGraphic = new Graphic(mDestinationPoint, pinDestinationSymbol);
                    mGraphicsOverlay.getGraphics().add(destinationGraphic);
                }
            });
            pinDestinationSymbol.setOffsetY(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //[DocRef: END]
        mRouteSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 5);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mLocationDisplay.startAsync();
        } else {
            Toast.makeText(MainActivity.this, "Permission Denied", Toast
                    .LENGTH_SHORT).show();
        }
    }

    private void addControls() {
        mMapView = findViewById(R.id.mapView);
        btnLocation = findViewById(R.id.btnLocation);
        btnSearchHospital = findViewById(R.id.btnSearchHospital);
        progressBar = findViewById(R.id.progressBar);
        lvDirection = findViewById(R.id.lvDirection);
        txtTotalTime = findViewById(R.id.txtTotalTime);
        txtTotalLength = findViewById(R.id.txtTotalLength);
        // Set panel hidden when create app
        slidingLayout = findViewById(R.id.sliding_layout);
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    @Override
    protected void onPause() {
        mMapView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.resume();

        checkFirstRunApp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.dispose();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.actionbar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void searchRoute(final Point st1, final Point st2) {
        progressBar.setVisibility(View.VISIBLE);

        mRouteTask = new RouteTask(MainActivity.this, getString(R.string.routing_service));
        mRouteTask.setCredential(userCredential);
        mRouteTask.loadAsync();
        mRouteTask.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                if (mRouteTask.getLoadError() == null && mRouteTask.getLoadStatus() == LoadStatus.LOADED) {
                    Toast.makeText(MainActivity.this, "routeTask loaded", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "routeTask fail loaded", Toast.LENGTH_SHORT).show();
                }
            }
        });
        final ListenableFuture<RouteParameters> listenableFuture = mRouteTask.createDefaultParametersAsync();
        listenableFuture.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    if (listenableFuture.isDone()) {
                        int i = 0;
                        mRouteParams = listenableFuture.get();
                        mRouteParams.setReturnStops(true);
                        mRouteParams.setReturnDirections(true);

                        Stop stop1 = new Stop(st1);
                        Stop stop2 = new Stop(st2);

                        List<Stop> routeStops = new ArrayList<>();

                        routeStops.add(stop1);
                        routeStops.add(stop2);
                        mRouteParams.setStops(routeStops);

                        try {
                            RouteResult result = mRouteTask.solveRouteAsync(mRouteParams).get();
                            List routes = result.getRoutes();
                            mRoute = (Route) routes.get(0);
                            // create a route graphic
                            Graphic routeGraphic = new Graphic(mRoute.getRouteGeometry(), mRouteSymbol);
                            mGraphicsOverlay.getGraphics().add(routeGraphic);
                            // get directions
                            List<DirectionManeuver> directions = mRoute.getDirectionManeuvers();
                            String[] directionsArray = new String[directions.size()];
                            for (DirectionManeuver dm : directions) {
                                directionsArray[i++] = dm.getDirectionText();
                            }

                            ArrayAdapter directionAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, directionsArray);
                            lvDirection.setAdapter(directionAdapter);

                            txtTotalTime.setText(String.valueOf(Math.round(Math.round(mRoute.getTotalTime()))) + " phút");
                            if (mRoute.getTotalLength() < 1000) {
                                txtTotalLength.setText("(" + String.valueOf(Math.round(mRoute.getTotalLength())) + "m)");
                            } else if (mRoute.getTotalLength() < 5000) {
                                txtTotalLength.setText("(" + String.valueOf(Math.round(mRoute.getTotalLength() / 1000.0 * 10) / 10.0) + " km)");
                            } else {
                                txtTotalLength.setText("(" + String.valueOf(Math.round(mRoute.getTotalLength() / 1000)) + " km)");
                            }

                            if (slidingLayout.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED) {
                                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }
}